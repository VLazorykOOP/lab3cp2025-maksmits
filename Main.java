import java.util.*;
// --- Абстрактна фабрика ---
interface Message {
    String getContent();
    void accept(MessageVisitor visitor);
}
class EmailMessage implements Message {
    private String content;
    public EmailMessage(String content) { this.content = content; }
    public String getContent() { return "Email: " + content; }
    public void accept(MessageVisitor visitor) { visitor.visit(this); }
}
class SMSMessage implements Message {
    private String content;
    public SMSMessage(String content) { this.content = content; }
    public String getContent() { return "SMS: " + content; }
    public void accept(MessageVisitor visitor) { visitor.visit(this); }
}
interface MessageFactory {
    Message createEmail(String content);
    Message createSMS(String content);
}
class SimpleMessageFactory implements MessageFactory {
    public Message createEmail(String content) { return new EmailMessage(content); }
    public Message createSMS(String content) { return new SMSMessage(content); }
}
// --- Adapter ---
class LegacyConsolePrinter {
    public void printRaw(String text) {
        System.out.println(">> " + text);
    }
}
interface MessagePrinter {
    void print(Message message);
}
class PrinterAdapter implements MessagePrinter {
    private LegacyConsolePrinter legacyPrinter = new LegacyConsolePrinter();
    public void print(Message message) {
        legacyPrinter.printRaw(message.getContent());
    }
}
// --- Visitor ---
interface MessageVisitor {
    void visit(EmailMessage email);
    void visit(SMSMessage sms);
}
class CountVisitor implements MessageVisitor {
    public void visit(EmailMessage email) {
        System.out.println("[Email символів]: " + email.getContent().length());
    }
    public void visit(SMSMessage sms) {
        System.out.println("[SMS символів]: " + sms.getContent().length());
    }
}
public class Main {
    public static void main(String[] args) {
        MessageFactory factory = new SimpleMessageFactory();
        List<Message> messages = new ArrayList<>();
        messages.add(factory.createEmail("Привіт, студенте!"));
        messages.add(factory.createSMS("Твоя оцінка: 52"));
        // Вивід через адаптер
        MessagePrinter printer = new PrinterAdapter();
        for (Message msg : messages) {
            printer.print(msg);
        }
        // Візит до повідомлень
        MessageVisitor visitor = new CountVisitor();
        for (Message msg : messages) {
            msg.accept(visitor);
        }
    }
}