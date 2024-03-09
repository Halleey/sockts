package texto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ServidorClienteHandler implements Runnable {
    private Socket socket;
    private DataInputStream entrada;
    private DataOutputStream saida;
    private String nome;

    public ServidorClienteHandler(Socket socket, DataOutputStream saida) {
        this.socket = socket;
        this.saida = saida;
        try {
            this.entrada = new DataInputStream(socket.getInputStream());
            // Leia o nome do usuário ao se conectar
            this.nome = entrada.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String mensagem = entrada.readUTF();
                System.out.println("Mensagem recebida do cliente " + nome + ": " + mensagem);

                // Enviar mensagem recebida para todos os clientes com o nome do remetente
                Servidor.broadcast(nome + ": " + mensagem);
            }
        } catch (IOException e) {
            // Se ocorrer uma exceção de E/S, provavelmente o cliente se desconectou
            // Remover o cliente da lista de clientes conectados
            Servidor.removerCliente(saida);
            System.out.println("Cliente " + nome + " desconectado");
            // Fechar o socket e a thread
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}