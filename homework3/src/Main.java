import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

public class Main {
    public static void main(String[] args) {
        String[] fio = new String[3];
        String birthDate;
        String gender;
        Long phoneNumber;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello and welcome!");
        System.out.println("для отладки введите 1 любой символ");
        String line = scanner.nextLine();

        if (line.length() == 1){
            line = "22.10.1999 Ya 5645646547 Sla m Ig";
        }

        String[] splitedLine = line.split(" ");

        try {
            CheckLenData(splitedLine); // Если данные не корректны, то проброситься исключение

            phoneNumber = GetPhoneNumber(splitedLine);
            System.out.println(phoneNumber.toString());

            gender = GetGender(splitedLine);
            System.out.println(gender);

            birthDate = GetBirthday(splitedLine);
            System.out.println(birthDate);

            fio = GetName(splitedLine);
            System.out.println(fio[0] + " " + fio[1] + " "+ fio[2]);

            String[] data = new String[6];
            data[0] = fio[0];
            data[1] = fio[1];
            data[2] = fio[2];
            data[3] = birthDate;
            data[4] = phoneNumber.toString();
            data[5] = gender;

            if (write(data)){
                System.out.println("Данные записаны");
            }
            else {
                System.out.println("Неожиданная ошибка записи которая не бросила исключений.");
            }
        }
        catch (ExceptionFormatData e){
            System.out.println(e.getMessage());
        }
    }

    static boolean CheckLenData (String[] line){
        if (line.length < 6){
            throw new ExceptionFormatData("Вы ввели меньше данных чем надо. " + line.length);
        }
        if (line.length > 6){
            throw new ExceptionFormatData("Вы ввели больше данных чем надо. " + line.length);
        }
        return true;
    }

    static boolean CheckBirthday(String str) {
        String[] splitedLine = str.split("\\.");
        Integer[] tmp = new Integer[splitedLine.length];

        int i = 0;
        for (String el: splitedLine) {
            tmp[i++] = Integer.parseInt(el);
        }

        if (tmp[0] < 1 || tmp[0] > 31){
            throw new ExceptionFormatData("День выходит за границы допустимого.");
        }
        if (tmp[1] < 1 || tmp[1] > 12){
            throw new ExceptionFormatData("Месяц выходит за границы допустимого");
        }
        if (tmp[2] < 1900 || tmp[2] > 2024){
            throw new ExceptionFormatData("Год выходит за границы допустимого");
        }
        // Если все прошло хорошо, тогда true!
        return true;
    }

    static String GetGender(String[] splitedLine){
        for (String str: splitedLine) {
            if (str.length() == 1 && str.matches("[fFmMжЖмМ]")){
                return str;
            }
        }
        throw new ExceptionFormatData("Не нашли какой пол. (m/f, м/ж)");
    }

    static String GetBirthday (String[] splitedLine){
        for (String str: splitedLine) {
            if (str.matches("[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}")){ // регулярное выражение, 3 блока чисел по 2/2/4 числа от 0 до 9
                if (CheckBirthday(str)){
                    return str;
                }
            }
            else {
                throw new ExceptionFormatData("С датой беда");
            }
        }
        throw new ExceptionFormatData("Не нашли дату рождения. (dd.mm.yyyy)");
    }

    static Long GetPhoneNumber(String[] splitedLine) {

        for (String str: splitedLine) {
            try {
                Long tel = Long.parseLong(str);

                // Если не упало исключение, проверим длину строки номера телефона
                if(str.length() != 10) {
                    throw new ExceptionFormatData("Не верное кол-во знаков в номере телефона.");
                }
                return tel;
            }
            catch (NumberFormatException e){
                // перехватываем исключения парсинга для поиска номера телефона
                e.getMessage();
            }
        }
        // Если добрались сюда, значит не один из блоков не соответствует номеру телефона
        throw new ExceptionFormatData("Телефона в списке данных не нашлось. Вы точно вводили номер телефона?");
    }

    static String[] GetName(String[] splitedLine){
        // Предполагаем, что фамилия имя и отчество идут в таком порядке
        int index = 0;
        String[] tmp = new String[3];
        for (String str: splitedLine) {
            if (str.matches("\\D+") && str.length() > 1){
                tmp[index++] = str;
            }
        }

        if (index == 3) {
            return tmp;
        }
        else {
            throw new ExceptionFormatData("Фио не смогли определить в массиве");
        }
    }

    public static boolean write(String[] data) {
        String fileName =data[0] + ".txt";
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("File couldn't been created.");
        }

        try (FileWriter fileWriter = new FileWriter(fileName, true)){
            String line = data[0] + " " +
            data[1] + " " +
            data[2] + " " +
            data[3] + " " +
            data[4] + " " +
            data[5] + "\n";

            fileWriter.write(line);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}