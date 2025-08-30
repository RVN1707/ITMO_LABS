package org.example;

import org.example.Managers.CollectionManager;
import org.example.Managers.CommandManager;
import org.example.Managers.FileManager;
import org.example.exemplars.Route;
import java.net.InetSocketAddress;
import org.example.Commands.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.logging.*;
import java.util.Iterator;
import java.io.*;

public class ServerLogic {

    private static final Logger logger = Logger.getLogger(ServerLogic.class.getName());
    private final InetSocketAddress address;
    private final int port;
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private final FileManager fileManager;

    public ServerLogic(int port, String fileName) {
        this.port = port;
        this.address = new InetSocketAddress(port);
        this.collectionManager = new CollectionManager();
        this.commandManager = new CommandManager();
        this.fileManager = new FileManager(fileName, collectionManager);

        configureLogger();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fileManager.saveCollectionToCsv();
            logger.info("Коллекция успешно сохранена при завершении работы сервера");
        }));
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

            fileManager.readCSVFile();

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
                    keys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    try {
                        if (key.isAcceptable()) {
                            acceptClient(key);
                        }

                        if (key.isReadable()) {
                            readRequest(key);
                        }

                        if (key.isWritable()) {
                            sendResponse(key);
                        }

                    } catch (IOException e) {
                        logger.warning("Клиент отключился: " + key.channel().toString());
                        key.cancel();
                        try { key.channel().close(); } catch (IOException io) {}

                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Непредвиденная ошибка с клиентом " + key.channel().toString(), e);
                        key.cancel();
                        try { key.channel().close(); } catch (IOException io) {}
                    }
                }
            }
        } catch (java.net.BindException e) {
            logger.severe("Не удалось запустить сервер: порт " + port + " уже занят");
            System.exit(1);
        } catch (IOException e) {
            logger.severe("Ошибка инициализации сервера: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Фатальная ошибка сервера", e);
            System.exit(1);
        }
    }

    private final class ClientSession {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        ByteBuffer dataBuffer = null;
        int expectedLength = -1;

        Request request;
        Response response;
        ByteBuffer responseBuffer = null;

        void resetForNextRequest() {
            lengthBuffer.clear();
            dataBuffer = null;
            expectedLength = -1;
        }

        void resetFull() {
            resetForNextRequest();
            request = null;
            response = null;
            responseBuffer = null;
        }
    }

    private void acceptClient(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        SelectionKey clientKey = clientChannel.register(key.selector(), SelectionKey.OP_READ);
        clientKey.attach(new ClientSession());

        logger.info("[SERVER] Новый клиент подключён");
    }

    private void readRequest(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ClientSession session = (ClientSession) key.attachment();

        if (session.expectedLength == -1) {
            int bytesRead = channel.read(session.lengthBuffer);
            if (bytesRead == -1) {
                channel.close();
                key.cancel();
                return;
            }

            if (!session.lengthBuffer.hasRemaining()) {
                session.lengthBuffer.flip();
                session.expectedLength = session.lengthBuffer.getInt();

                if (session.expectedLength <= 0 || session.expectedLength > 1000000000) {
                    logger.warning("Невалидная длина сообщения: " + session.expectedLength);
                    channel.close();
                    key.cancel();
                    return;
                }

                session.dataBuffer = ByteBuffer.allocate(session.expectedLength);
            }

        } else {
            int bytesRead = channel.read(session.dataBuffer);
            if (bytesRead == -1) {
                channel.close();
                key.cancel();
                return;
            }

            if (!session.dataBuffer.hasRemaining()) {
                session.dataBuffer.flip();
                try (ObjectInputStream ois = new ObjectInputStream(
                        new ByteArrayInputStream(session.dataBuffer.array()))) {
                    session.request = (Request) ois.readObject();
                    logger.info("Полученная команда: " + session.request.getCommandName());
                } catch (ClassNotFoundException | IOException e) {
                    logger.log(Level.SEVERE, "Ошибка чтения запроса", e);
                    channel.close();
                    key.cancel();
                    return;
                }
                session.resetForNextRequest();
            }
        }

        if (session.request != null) {
            processRequest(key);
        }
    }

    private void processRequest(SelectionKey key) {
        ClientSession session = (ClientSession) key.attachment();
        Request request = session.request;

        if (request == null) {
            logger.warning("Запрос пустой");
            return;
        }

        var commandName = request.getCommandName();
        var commandStrArg = request.getCommandStrArg();
        var commandObjArg = (Route) request.getCommandObjArg();

        if (commandManager.getCommands().containsKey(commandName)) {
            try {
                session.response = commandManager.getCommands().get(commandName)
                        .execute(commandStrArg, commandObjArg);
            } catch (FileNotFoundException e) {
                logger.severe("[SERVER] Файл не найден при выполнении команды: " + e.getMessage());
                session.response = new Response("Ошибка: файл не найден", "");
            }
        } else {
            session.response = new Response("Команда не найдена. Используйте help для справки", "");
        }
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void sendResponse(SelectionKey key) throws IOException {
        ClientSession session = (ClientSession) key.attachment();
        SocketChannel clientChannel = (SocketChannel) key.channel();

        if (session.responseBuffer == null && session.response != null) {
            try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                 ObjectOutputStream clientDataOut = new ObjectOutputStream(bytes)) {

                clientDataOut.writeObject(session.response);
                byte[] byteResponse = bytes.toByteArray();

                session.responseBuffer = ByteBuffer.allocate(4 + byteResponse.length);
                session.responseBuffer.putInt(byteResponse.length);
                session.responseBuffer.put(byteResponse);
                session.responseBuffer.flip();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Ошибка сериализации ответа", e);
                session.responseBuffer = null;
                session.response = null;
                key.interestOps(SelectionKey.OP_READ);
                return;
            }
        }

        if (session.responseBuffer != null) {
            int bytesWritten = clientChannel.write(session.responseBuffer);
            logger.finer("Отправлено " + bytesWritten + " байт клиенту");

            if (!session.responseBuffer.hasRemaining()) {
                session.resetFull();
                key.interestOps(SelectionKey.OP_READ);
            } else {
                key.interestOps(SelectionKey.OP_WRITE);
            }
        }
    }
}