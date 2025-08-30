package org.example;

import org.example.Commands.*;
import org.example.Managers.CollectionManager;
import org.example.Managers.CommandManager;
import org.example.Managers.DBManager;
import org.example.exemplars.Route;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

public class ServerLogic {
    private static final Logger logger = Logger.getLogger(ServerLogic.class.getName());
    private final InetSocketAddress address;
    private final int port;
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private final ExecutorService requestProcessingPool;
    private final ExecutorService responseSendingPool;

    public ServerLogic(int port) {
        this.port = port;
        this.address = new InetSocketAddress(port);
        this.collectionManager = new CollectionManager();
        this.commandManager = new CommandManager();
        this.requestProcessingPool = Executors.newFixedThreadPool(10);
        this.responseSendingPool = Executors.newCachedThreadPool();
        configureLogger();
    }

    private void configureLogger() {
        try {
            logger.setUseParentHandlers(false);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Ошибка при настройке логгера");
        }
    }

    public void run() {
        try {
            commandManager.addCommand("add", new AddCommand(collectionManager));
            commandManager.addCommand("addifmin", new AddIfMinCommand(collectionManager));
            commandManager.addCommand("clear", new ClearCommand(collectionManager));
            commandManager.addCommand("filtercontainsname", new FilterContainsNameCommand(collectionManager));
            commandManager.addCommand("help", new HelpCommand(commandManager));
            commandManager.addCommand("info", new InfoCommand(collectionManager));
            commandManager.addCommand("printfieldascendingdistance", new PrintFieldAscendingDistanceCommand(collectionManager));
            commandManager.addCommand("printfielddescendingdistance", new PrintFieldDescendingDistanceCommand(collectionManager));
            commandManager.addCommand("removebyid", new RemoveByIdCommand(collectionManager));
            commandManager.addCommand("removegreater", new RemoveGreaterCommand(collectionManager));
            commandManager.addCommand("removelower", new RemoveLowerCommand(collectionManager));
            commandManager.addCommand("show", new ShowCommand(collectionManager));
            commandManager.addCommand("update", new UpdateIdCommand(collectionManager));

            DBManager.load(collectionManager);

            Selector selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(address);
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("[SERVER] Сервер запущен на порту " + port);

            while (true) {
                int readyChannels = selector.select(1000);
                if (readyChannels == 0) continue;

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (!key.isValid()) {
                        keys.remove();
                        continue;
                    }

                    try {
                        if (key.isAcceptable()) {
                            acceptClient(key);
                        }
                        if (key.isReadable()) {
                            new Thread(() -> {
                                try {
                                    readRequest((SocketChannel) key.channel(), key);
                                } catch (IOException | ClassNotFoundException e) {
                                    // Просто логируем и закрываем
                                    logger.info("Клиент отключился во время чтения: " + e.getMessage());
                                    try {
                                        key.channel().close();
                                    } catch (IOException ignored) {}
                                    key.cancel();
                                }
                            }).start();
                            key.interestOps(0);
                        }
                    } catch (IOException e) {
                        logger.info("Ошибка при обработке клиента: " + e.getMessage());
                        try {
                            key.channel().close();
                        } catch (IOException ignored) {}
                        key.cancel();
                    } catch (Throwable t) {
                        logger.log(Level.SEVERE, "[SERVER] Непредвиденная ошибка", t);
                        try {
                            key.channel().close();
                        } catch (IOException ignored) {}
                        key.cancel();
                    }
                    keys.remove();
                }
            }
        } catch (Exception e) {
            logger.severe("[SERVER] Фатальная ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        } finally {
            logger.info("Коллекция сохранена перед завершением работы сервера");
            requestProcessingPool.shutdown();
            responseSendingPool.shutdown();
        }
    }

    private void acceptClient(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(key.selector(), SelectionKey.OP_READ);
        logger.info("[SERVER] Новый клиент подключён");
    }

    private void readRequest(SocketChannel clientChannel, SelectionKey key) throws IOException, ClassNotFoundException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        int totalRead = 0;
        while (totalRead < 4) {
            int read = clientChannel.read(lengthBuffer);
            if (read == -1) {
                logger.info("Клиент закрыл соединение во время чтения длины");
                try {
                    clientChannel.close();
                } catch (IOException ignored) {}
                key.cancel();
                return;
            }
            totalRead += read;
        }
        lengthBuffer.flip();
        int length = lengthBuffer.getInt();
        logger.finer("[SERVER] Получена длина запроса: " + length);

        ByteBuffer dataBuffer = ByteBuffer.allocate(length);
        totalRead = 0;
        while (totalRead < length) {
            int read = clientChannel.read(dataBuffer);
            if (read == -1) {
                logger.info("Клиент закрыл соединение во время чтения данных");
                try {
                    clientChannel.close();
                } catch (IOException ignored) {}
                key.cancel();
                return;
            }
            totalRead += read;
        }
        dataBuffer.flip();

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBuffer.array()))) {
            Request request = (Request) ois.readObject();
            logger.fine("[SERVER] Запрос успешно десериализован: " + request.getCommandName());
            requestProcessingPool.submit(() -> processRequest(request, key));
        }
    }

    private void processRequest(Request request, SelectionKey key) {
        Response response;
        if (request.getCommandName() == null) {
            var user = request.getUser();
            if (!request.isRegisterRequired()) {
                if (DBManager.checkUserExistence(user.getUsername())) {
                    if (DBManager.checkUserPassword(user)) {
                        response = new Response("Привет, " + user.getUsername() + "\n", true);
                        logger.info("Пользователь " + user.getUsername() + " успешно аутентифицирован");
                    } else {
                        response = new Response("Пароль введён неверно", false);
                        logger.info("Пользователь " + user.getUsername() + " неверно ввёл пароль");
                    }
                } else {
                    response = new Response("Пользователя " + user.getUsername() + " не существует", false);
                    logger.info("Пользователя " + user.getUsername() + " не существует");
                }
            } else {
                if (DBManager.checkUserExistence(user.getUsername())) {
                    response = new Response("Такой пользователь есть в системе(", false);
                } else {
                    DBManager.addUser(user);
                    response = new Response("Добро пожаловать, " + user.getUsername() + "\n", true);
                    logger.info("Пользователь " + user.getUsername() + " успешно зарегистрирован");
                }
            }
        } else {
            var commandName = request.getCommandName();
            var commandStrArg = request.getCommandStrArg();
            var commandObjArg = (Route) request.getCommandObjArg();
            var user = request.getUser();
            if (commandManager.getCommands().containsKey(commandName)) {
                response = commandManager.getCommands().get(commandName).execute(user, commandStrArg, commandObjArg);
            } else {
                response = new Response("Команда не найдена. Используйте help для справки", "");
            }
        }

        Response finalResponse = response;
        responseSendingPool.submit(() -> sendResponse(finalResponse, key));
    }

    private void sendResponse(Response response, SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream clientDataOut = new ObjectOutputStream(bytes)) {
            clientDataOut.writeObject(response);
            byte[] byteResponse = bytes.toByteArray();
            ByteBuffer dataLength = ByteBuffer.allocate(4).putInt(byteResponse.length);
            dataLength.flip();
            while (dataLength.hasRemaining()) {
                int written = clientChannel.write(dataLength);
                if (written == -1) {
                    logger.info("Клиент отключился при отправке длины ответа");
                    try {
                        clientChannel.close();
                    } catch (IOException ignored) {}
                    key.cancel();
                    return;
                }
            }
            ByteBuffer dataBuffer = ByteBuffer.wrap(byteResponse);
            while (dataBuffer.hasRemaining()) {
                int written = clientChannel.write(dataBuffer);
                if (written == -1) {
                    logger.info("Клиент отключился при отправке тела ответа");
                    try {
                        clientChannel.close();
                    } catch (IOException ignored) {}
                    key.cancel();
                    return;
                }
            }
            logger.finer("[SERVER] Ответ отправлен клиенту");
            key.interestOps(SelectionKey.OP_READ);

        } catch (IOException e) {
            logger.info("Ошибка при отправке ответа: " + e.getMessage());
            try {
                clientChannel.close();
            } catch (IOException ignored) {}
            key.cancel();
        }
    }
}