package org.example;

import org.example.RuntimeParsers.RouteInteractiveParser;
import org.example.exceptions.ScriptRecursionException;
import java.security.NoSuchAlgorithmException;
import java.nio.file.InvalidPathException;
import java.nio.channels.SocketChannel;
import static java.lang.System.exit;
import org.example.exemplars.Route;
import java.nio.file.Paths;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.*;
import java.net.*;
import java.io.*;

public class ClientLogic {
    private final Set<Path> scriptsNames = new TreeSet<>();
    private final int port;
    private final InetAddress host;
    private SocketChannel channel;

    Scanner scanner = new Scanner(System.in);
    User user;

    public ClientLogic(InetAddress host, int port) {
        this.port = port;
        this.host = host;
    }

    public void run() throws IOException, ClassNotFoundException {
        while (true) {
            try {
                SocketAddress address = new InetSocketAddress(host, port);
                channel = SocketChannel.open();
                if (channel.connect(address)) {
                    System.out.println("Подключение установлено.");
                    break;
                }
            } catch (IOException e) {
                System.out.println("Сервер недоступен. Повторная попытка через 5 секунд...");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.out.println("Ожидание прервано. Завершение работы клиента.");
                exit(1);
            }
        }
        authenticateUser();

        try {
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    System.out.println("Остановка клиента через консоль");
                    exit(1);
                }

                String inputLine = scanner.nextLine().trim();

                if (inputLine.isEmpty()) {
                    continue; // просто снова выводим приглашение
                }

                String[] input = inputLine.split(" ", 2); // Делим на команду и аргументы
                String command = input[0].trim();
                String[] arguments = input.length > 1 ? input[1].trim().split(" ") : new String[0];

                processUserPrompt(command, arguments);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Остановка клиента через консоль");
            exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Объект поступивший в ответ от сервера не найден");
        } catch (SocketException e) {
            System.out.println("Сервер был остановлен во время обработки вашего запроса. Пожалуйста, повторите попытку позже");
            exit(1);
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
            exit(1);
        }
    }

    private void authenticateUser() {
        try {
            while (true) {
                System.out.println("Введите логин: ");
                String username = scanner.nextLine().trim();

                System.out.println("Введите пароль: ");
                String password = scanner.nextLine().trim();

                user = new User(username, PasswordHasher.getHash(password));

                Request userAuthenticationRequest = new Request(user, false);
                Response response = sendAndReceive(userAuthenticationRequest);

                if (response.getuserAuthentication()) {
                    printResponse(response);
                    break;
                } else {
                    printResponse(response);
                    if (response.getMessage().equals("Пользователя " + user.getUsername() + " не существует")) {
                        System.out.println("Если вы хотите зарегистрироваться, нажмите 'y'");
                        String ans = scanner.nextLine().trim();
                        if (ans.equalsIgnoreCase("y")) {
                            while (!registerUser()) {
                            }
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Остановка клиента через консоль");
            exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            exit(1);
        }
    }

    private boolean registerUser() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        try {
            System.out.println("Введите логин: ");
            String username = scanner.nextLine().trim();

            System.out.println("Введите пароль: ");
            String password = scanner.nextLine().trim();

            user = new User(username, PasswordHasher.getHash(password));

            Request userAuthenticationRequest = new Request(user, true);
            Response response = sendAndReceive(userAuthenticationRequest);
            printResponse(response);
            return response.getuserAuthentication();

        } catch (NoSuchElementException e) {
            System.out.println("Остановка клиента через консоль");
            exit(1);
            return false;
        }
    }

    private void processUserPrompt(String command, String[] arguments) throws IOException, ClassNotFoundException {
        Request request;
        if (command.equalsIgnoreCase("add") ||
                command.equalsIgnoreCase("update") ||
                command.equalsIgnoreCase("removegreater") ||
                command.equalsIgnoreCase("addifmin") ||
                command.equalsIgnoreCase("removelower")) {

            Route objArgument = new RouteInteractiveParser(user.getUsername()).parse();
            request = new Request(user, command, arguments, objArgument);
            Response response = sendAndReceive(request);
            printResponse(response);

        } else if (command.equalsIgnoreCase("exit")) {
            System.out.println("Работа клиентского приложения завершена");
            exit(0);

        } else if (command.equalsIgnoreCase("execute_script")) {
            String path = String.join(" ", arguments).trim();
            if (path.isEmpty()) {
                System.out.println("Не указан путь к скрипту");
            } else {
                executeScript(path);
            }

        } else {
            request = new Request(user, command, arguments);
            Response response = sendAndReceive(request);
            printResponse(response);
        }
    }

    private Response sendAndReceive(Request request) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objOut = new ObjectOutputStream(byteStream)) {
            objOut.writeObject(request);
        }
        byte[] requestData = byteStream.toByteArray();

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4).putInt(requestData.length);
        lengthBuffer.flip();
        channel.write(lengthBuffer);

        ByteBuffer dataBuffer = ByteBuffer.wrap(requestData);
        while (dataBuffer.hasRemaining()) {
            channel.write(dataBuffer);
        }

        ByteBuffer responseLengthBuffer = ByteBuffer.allocate(4);
        while (responseLengthBuffer.hasRemaining()) {
            channel.read(responseLengthBuffer);
        }
        responseLengthBuffer.flip();
        int responseLength = responseLengthBuffer.getInt();

        ByteBuffer responseBuffer = ByteBuffer.allocate(responseLength);
        while (responseBuffer.hasRemaining()) {
            channel.read(responseBuffer);
        }

        try (ObjectInputStream objIn = new ObjectInputStream(
                new ByteArrayInputStream(responseBuffer.array()))) {
            return (Response) objIn.readObject();
        }
    }

    private void printResponse(Response response) {
        System.out.println(response.getMessage());
        String collection = response.getCollectionToStr();
        if (collection != null && !collection.isEmpty()) {
            System.out.println(collection);
        }
    }

    private void executeScript(String path){
        if (path.isBlank()) {
            System.out.println("Неверные аргументы команды");
            return;
        }

        try {
            Path pathToScript = Paths.get(path);
            Path scriptFile = pathToScript.getFileName();

            Scanner scriptScanner = new Scanner(pathToScript);
            this.scanner = scriptScanner;
            scriptsNames.add(scriptFile);

            while (scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] input = line.split(" ", 2);
                String command = input[0].trim();
                String[] arguments = input.length > 1 ? input[1].trim().split(" ") : new String[0];

                if (command.equalsIgnoreCase("execute_script")) {
                    try {
                        Path nestedScript = Paths.get(String.join(" ", arguments)).getFileName();
                        if (scriptsNames.contains(nestedScript)) {
                            throw new ScriptRecursionException("Рекурсивный вызов скрипта запрещён: " + nestedScript);
                        }
                        executeScript(String.join(" ", arguments));
                    } catch (ScriptRecursionException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    processUserPrompt(command, arguments);
                }
            }

            scriptsNames.remove(scriptFile);
            this.scanner = new Scanner(System.in);
            System.out.println("Скрипт " + scriptFile + " успешно выполнен");

        } catch (FileNotFoundException e) {
            System.out.println("Файл " + path + " не найден");
        } catch (SecurityException e) {
            System.out.println("Нет прав на чтение файла " + path);
        } catch (InvalidPathException e) {
            System.out.println("Некорректный путь: " + path);
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода при чтении файла " + path);
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении скрипта: " + e.getMessage());
        }
    }
}