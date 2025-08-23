package org.example;

import org.example.RuntimeParsers.RouteInteractiveParser;
import org.example.exceptions.ScriptRecursionException;
import org.example.exemplars.Route;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static java.lang.System.exit;

public class ClientLogic {
    private final Set<Path> scriptsNames = new TreeSet<>();
    private final int port;
    private final InetAddress host;
    private SocketChannel channel;
    Scanner scanner = new Scanner(System.in);

    public ClientLogic(InetAddress host, int port) { //Конструктор
        this.port = port;
        this.host = host;
    }

    public void run() {
        while (true) { //Процесс подключения к серверу
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
                Thread.sleep(5000); //Ждем 5 секунд
            } catch (InterruptedException e) {
                System.out.println("Ожидание прервано. Завершение работы клиента.");
                exit(1);
            }
        }

        try { //Чтение и обработка команд
            while (true) {
                System.out.print("> ");
                try {

                    while (scanner.hasNext()) {
                        String[] input = (scanner.nextLine() + " ").trim().split(" ");
                        String command = input[0].trim();
                        String[] arguments =  Arrays.copyOfRange(input, 1, input.length);
                        processUserPrompt(command, arguments);
                        System.out.print("> ");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Остановка клиента через консоль");
                    exit(1);
                } catch (ClassNotFoundException e) {
                    System.out.println("Объект поступивший в ответ от сервера не найден");
                } catch (SocketException e) {
                    System.out.println("Сервер был остановлен во время обработки вашего запроса. Пожалуйста, повторите попытку позже");
                    exit(1);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
            exit(1);
        }
    }

    private void processUserPrompt(String command, String[] arguments) throws IOException, ClassNotFoundException {
        Request request;
        if (command.equalsIgnoreCase("add") || command.equalsIgnoreCase("update") || command.equalsIgnoreCase("remove_greater") || command.equalsIgnoreCase("add_if_min") || command.equalsIgnoreCase("remove_lower")) {
            Route objArgument = new RouteInteractiveParser().parse();
            request = new Request(command, arguments, objArgument);
            Response response = sendAndReceive(request);  // Получаем ответ
            printResponse(response);
        } else if (command.equalsIgnoreCase("exit")) {
            System.out.println("Работа клиентского приложения завершена");
            exit(0);
        } else if (command.equalsIgnoreCase("execute_script")) {
            String path = String.join(" ", arguments).trim(); // Собираем все аргументы в путь
            if (path.isEmpty()) {
                System.out.println("Не указан путь к скрипту");
            } else {
                executeScript(path);
            }

        } else {
            request = new Request(command, arguments);
            Response response = sendAndReceive(request);
            printResponse(response);
        }
    }

    private Response sendAndReceive(Request request) throws ClassNotFoundException {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objOut = new ObjectOutputStream(byteStream)) {
                objOut.writeObject(request);
            }
            byte[] requestData = byteStream.toByteArray();

            ByteBuffer lengthBuffer = ByteBuffer.allocate(4).putInt(requestData.length);

            lengthBuffer.flip();

            try {
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
            } catch (ClosedChannelException e) {
                System.err.println("Канал был закрыт: " + e.getMessage());
                throw e;  // Можно пробросить дальше или обработать иначе
            }
        } catch (IOException e) {
            // Обработка других IO ошибок
            throw new RuntimeException("Ошибка ввода-вывода", e);
        }
    }
    private void printResponse(Response response) {
        System.out.println(response.getMessage());
        String collection = response.getCollectionToStr();
        try{
            if (!collection.isEmpty()){
                System.out.println(collection);
            }
        } catch (NullPointerException ignored){}
    }

    private void executeScript(String path) throws ClassNotFoundException {
        if (path.isBlank()) {
            System.out.println("Неверные аргументы команды");
        } else {
            try {
                Path pathToScript = Paths.get(path);
                Path scriptFile = pathToScript.getFileName();

                Scanner scriptScanner = new Scanner(pathToScript);
                if (!scriptScanner.hasNext()) throw new NoSuchElementException();

                this.scanner=scriptScanner;
                scriptsNames.add(scriptFile);

                do {
                    var command = "";
                    String[] input = (scriptScanner.nextLine() + " ").trim().split(" ");
                    command = input[0].trim();

                    while (scriptScanner.hasNextLine() && command.isEmpty()) {
                        input = (scriptScanner.nextLine() + " ").trim().split(" ", 2);
                        command = input[0].trim();
                    }

                    if (command.equalsIgnoreCase("execute_script")) {
                        try {
                            Path scriptNameFromArgument = Paths.get(input[1]).getFileName();

                            if (scriptsNames.contains(scriptNameFromArgument)) {
                                throw new ScriptRecursionException("Один и тот же скрипт не может выполнятся рекурсивно");
                            }
                            executeScript(input[1]);

                        } catch (ScriptRecursionException e) {
                            System.out.println(e.getMessage());
                        }

                    } else {
                        processUserPrompt(command, Arrays.copyOfRange(input, 1, input.length));
                    }

                } while (scriptScanner.hasNextLine());

                scriptsNames.remove(scriptFile);
                this.scanner = new Scanner(System.in);
                System.out.println("Скрипт " + scriptFile + " успешно выполнен");

            } catch (FileNotFoundException e) {
                System.out.println("Файл " + path + " не найден");
            } catch (NoSuchElementException e) {
                System.out.println("Файл " + path + " пуст");
            } catch (IllegalStateException e) {
                System.out.println("Непредвиденная ошибка");
            } catch (SecurityException e) {
                System.out.println("Недостаточно прав для чтения файла " + path);
            } catch (IOException e) {
                System.out.println("Ошибка ввода/вывода");
                System.out.println(e.getClass().getName());
            } catch (InvalidPathException e) {
                System.out.println("Проверьте путь к файлу. В нём не должно быть лишних символов");
            }
        }
    }
}