package adapter_design_pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Интерфейс для оплаты в разных валютах
interface Payment {
    void pay(double amount);
}

// Реализация интерфейса Payment для оплаты в тенге
class TengePayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Оплачено " + amount + " тенге.");
    }
}

// Реализация интерфейса Payment для оплаты в долларах
class DollarPayment implements Payment {
    @Override
    public void pay(double amount) {
        double convertedAmount = amount * 452; // Конверсия суммы из долларов в тенге
        System.out.println("Оплачено " + convertedAmount + " тенге (принято " + amount + " долларов)");
    }
}

// Адаптер для оплаты, который преобразует валюту и делегирует действие реальной реализации оплаты
class PaymentAdapter implements Payment {
    Payment payment;

    public PaymentAdapter(Payment payment) {
        this.payment = payment;
    }

    @Override
    public void pay(double amount) {
        payment.pay(amount);
    }
}

// Интертерфейс Observer для наблюдателя оплаты
interface PaymentObserver {
    void update(double amount);
}

// Класс ConcreteObserver для наблюдателя оплаты
class PaymentLogger implements PaymentObserver {
    @Override
    public void update(double amount) {
        System.out.println("Зафиксировано платежа на сумму " + amount + " тенге.");
    }
}

// Фабрика для создания объектов оплаты
class PaymentFactory {
    public static Payment createPayment(String currency) {
        if ("t".equalsIgnoreCase(currency)) {
            return new TengePayment(); // Оплата в тенге
        } else if ("d".equalsIgnoreCase(currency)) {
            return new PaymentAdapter(new DollarPayment()); // Оплата в долларах с использованием адаптера
        }
        return null;
    }
}

class PaymentExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите валюту для оплаты t (тенге) / d (доллары): ");
        String currency = scanner.nextLine();

        Payment payment = PaymentFactory.createPayment(currency); // Создаем объект оплаты с помощью фабрики

        if (payment != null) {
            System.out.println("Введите сумму для оплаты: ");
            double amount = scanner.nextDouble();
            payment.pay(amount); // Вызываем метод оплаты с указанной суммой
            notifyObservers(payment, amount);
        } else {
            System.out.println("Выбрана неподдерживаемая валюта.");
        }

        scanner.close(); // Закрываем сканнер после использования.
    }

    private static void notifyObservers(Payment payment, double amount) {
        PaymentObserver observer = new PaymentLogger();
        observer.update(amount);
        // Можно добавить других наблюдателей для выполнения других действий при оплате.
    }
}
