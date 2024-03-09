package texto;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    // Lista para armazenar referências dos sockets dos clientes conectados
    private static List<DataOutputStream> clientes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(54321);
        System.out.println("A porta 54321 foi aberta!");
        System.out.println("Servidor esperando receber mensagem de cliente...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado");

            // Obter fluxo de saída para enviar dados para o cliente
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
            // Adicionar o fluxo de saída do cliente à lista
            synchronized (clientes) {
                clientes.add(saida);
            }

            // Cria uma nova thread para lidar com a comunicação com este cliente
            Thread clientThread = new Thread(new ServidorClienteHandler(socket, saida));
            clientThread.start();
        }
    }

    // Método para enviar uma mensagem para todos os clientes conectados
    public static synchronized void broadcast(String mensagem) {
        for (DataOutputStream cliente : clientes) {
            try {
                cliente.writeUTF(mensagem);
                cliente.flush(); // Força a escrita imediata da mensagem
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para remover um cliente da lista quando ele se desconectar
    public static synchronized void removerCliente(DataOutputStream cliente) {
        clientes.remove(cliente);
    }
}