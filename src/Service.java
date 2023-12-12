import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Service {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите данные в произвольном порядке, разделенные пробелом(формат ввода даты dd.mm.yyyy)(формат ввода пола:f и m): ");
        String userDate = scanner.nextLine();

        try {
            reUserDate(userDate);
            System.out.println("Данные успешно обработаны и записаны в файл.");
        } catch (UserDateException | IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void reUserDate(String userDate) throws UserDateException, IOException {
        String [] reDates = userDate.split("\\s+");

        if (reDates.length != 6 ) {
            throw new UserDateException(" Неверное количество параметров. Требуется 6 параметров");
        }
        String fam = null;
        String imya = null;
        String otchestvo = null;
        String birthDate = null;
        long phoneNumber = 0;
        String gender = null;

        for (String part : reDates) {
            if (part.length() == 10 && part.matches(checkAndFormDate(part))){
                birthDate = part;
            } else if (part.length() == 11 && part.matches(checkAndFormNum(part))) {
                phoneNumber = Long.parseLong(part);
            } else if ((part.length() == 1 && part.matches(checkGender(part)))) {
                gender = part;
            } else if (part.length() > 1 && part.matches(checkFIO(part))) {
                 if (fam == null) {
                    fam = part;
                } else if (imya == null) {
                    imya = part;
                } else if (otchestvo == null) {
                    otchestvo = part;
                }
            }

        }

        String fileName = "ExceptHuman.txt";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(fam + " " + imya + " " + otchestvo + " " + birthDate + " " + phoneNumber + " " + gender);
            writer.newLine();
        }
    }

    private static String findStringPart(String[] reDates) throws UserDateException{
        for (String part: reDates){
            if (!part.matches("\\d+")){return part;}
        }
        throw new UserDateException("Не найдена чвсть данных с нечисловым форматом");
    }

    private static String checkAndFormDate(String date) throws UserDateException {
        String[] datasplit = date.split("\\.");
        if (datasplit.length != 3 && date.length() == 10) {
            throw new UserDateException("Некорректный ввод даты Рождения");
        }
        int dayB = Integer.parseInt(datasplit[0]);
        int monthB = Integer.parseInt((datasplit[1]));
        int yearB = Integer.parseInt(datasplit[2]);
        if (dayB < 1 || dayB > 31 || monthB < 1 || monthB > 12 || yearB < 1870 || yearB > 2024) {
            throw new UserDateException("Неверный ввод даты рождения");}

//        String dateRegex = "^(0[1-9]|[12][0-9]|3[01])\\.(0[]1-9]|1[0-2])\\.\\d{4}$";
//        if (!date.matches(dateRegex)){throw new UserDateException("Неверный формат даты. Используйте дд.мм.гггг");}
            return date;
        }

        private static String checkAndFormNum (String phoneN) throws UserDateException {
//            try {
//                return Long.parseLong(phoneN);
//            } catch (NumberFormatException e) {
//                throw new UserDateException("Неверный формат номера телефона.");
//            }
            String phoneReg = "[0-9]+";
            if (!phoneN.matches(phoneReg) && phoneN.length() > 11){throw new UserDateException ("Неверный формат номера телефона.");}
            return phoneN;
        }

        private static String checkGender (String gender) throws UserDateException {
            if (!gender.equals("f") && !gender.equals("m")) {
                throw new UserDateException ("Неверный формат пола. Используйте f или m");}
//            String genderReg = "^[fm]$";
//            if (!gender.matches(genderReg)) {
//                throw new UserDateException("Неверный формат пола. Используйте f или m");
//            }
            return gender;
        }

        private static String checkFIO(String date) throws UserDateException{
            if (date.matches("\\d+")) {
                throw new UserDateException("ФИО не должно содержать числа");
            }
            return date;
        }
}

class UserDateException extends Exception {
    public UserDateException(String message) {
        super(message);
    }
}