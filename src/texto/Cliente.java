package texto;
import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();

        Socket socket = new Socket("127.0.0.1", 54321);
        DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
        DataInputStream entrada = new DataInputStream(socket.getInputStream());

        // Enviar o nome do usuário para o servidor
        saida.writeUTF(nome);

        // Thread para receber mensagens do servidor
        Thread receiveThread = new Thread(() -> {
            try {
                while (true) {
                    String mensagemRecebida = entrada.readUTF();
                    System.out.println(mensagemRecebida);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start();

        // Loop para enviar mensagens para o servidor
        while (true) {
            System.out.print("Digite uma mensagem para enviar ao servidor: ");
            String mensagemParaEnviar = scanner.nextLine();
            saida.writeUTF(mensagemParaEnviar);
        }
    }
}