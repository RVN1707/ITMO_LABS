package org.example.Managers;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.example.FileParsers.FileParserRoute;
import org.example.exemplars.Route;
import java.util.Map;

public class FileManager {
    private final String Pathfile;
    private final CollectionManager collectionManager;

    public FileManager(String Pathfile, CollectionManager collectionManager) {
        this.Pathfile = System.getenv(Pathfile);
        System.out.println(this.Pathfile);
        List<String[]> csvDataList = new ArrayList<>();
        this.collectionManager=collectionManager;
    }

    public void readCSVFile() {
        ValidationManager validator = new ValidationManager();
        try (Reader reader = new FileReader(Pathfile.trim());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.newFormat(';').withFirstRecordAsHeader().withTrim())) {

            for (CSVRecord record : csvParser) {
                try {
                    Map<String, String> map = record.toMap();
                    FileParserRoute routeParser = new FileParserRoute();
                    routeParser.setMap(map);
                    Route route = routeParser.parse();
                    if (validator.isValidRoute(route, collectionManager)){
                        collectionManager.add(route);
                    }
                }catch (NullPointerException | NumberFormatException e) {}
            }
        } catch (FileNotFoundException e){
            System.err.println("Файл не найден");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка при чтении файла: " + Pathfile);
        }
    }
    public void saveCollectionToCsv(){

        Collection<Route> routes = collectionManager.getCollection();

        String[] headers = {"id", "name", "coords_x", "coords_y", "cr_date", "loc_from_x", "loc_from_y", "loc_from_z", "loc_to_x", "loc_to_y", "loc_to_z", "distance"};

        try (FileWriter fileWriter = new FileWriter(Pathfile);
             CSVPrinter csvPrinter = new CSVPrinter(fileWriter,
                     CSVFormat.DEFAULT
                             .withHeader(headers)
                             .withDelimiter(';'))) {

            for (Route route : routes) {

                Double coordX = null;
                Float coordY = null;
                if (route.getCoordinates() != null) {
                    coordX = route.getCoordinates().getX();
                    coordY = route.getCoordinates().getY();
                }

                Integer fromX = null;
                Double fromY = null;
                Float fromZ = null;
                if (route.getFrom() != null) {
                    fromX = route.getFrom().getX();
                    fromY = route.getFrom().getY();
                    fromZ = route.getFrom().getZ();
                }

                Long toX = null;
                Long toY = null;
                Float toZ = null;
                if (route.getTo() != null) {
                    toX = route.getTo().getX();
                    toY = route.getTo().getY();
                    toZ = route.getTo().getZ();
                }

                csvPrinter.printRecord(
                        route.getId(),
                        route.getName(),
                        coordX,
                        coordY,
                        route.getCreationDate(),
                        fromX,
                        fromY,
                        fromZ,
                        toX,
                        toY,
                        toZ,
                        route.getDistance()
                );
            }

            System.out.println("Коллекция успешно сохранена в CSV: " +
                    Paths.get(Pathfile).toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Ошибка сохранения в CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


