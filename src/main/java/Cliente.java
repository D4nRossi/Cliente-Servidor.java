import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Cliente implements Runnable{
    
    private Socket cliente;

    public Cliente(Socket cliente) {
        this.cliente = cliente;
    }
    
        public void run() {
        double codVal[][] = new double[8][200];
        String nomeProd[] = new String[200];
        int indiceVazio = 0, op;
        
        Servidor servidor = new Servidor(cliente);

        do {
            op = Integer.parseInt(JOptionPane.showInputDialog(
                    "                Sistema de controle de estoque\n\n\n"
                    + "                              Menu principal \n\n"
                    + "1  - Cadastrar produto \n" + ""
                    + "2  - Cadastrar entradas de produtos \n"
                    + "3  - Cadastrar vendas de produtos \n"
                    + "4  - Emitir relatório de vendas/faturamento \n"
                    + "5 - Mostrar todas as informações cadastradas \n"
                    + "6 - Sair do Sistema"));

            switch (op) {

                case 1:
                    
                    try {
            PrintStream saida;
            System.out.println("O cliente conectou ao servidor");
            
            servidor.cadastraProduto(nomeProd, codVal, indiceVazio);
            
            saida = new PrintStream(this.cliente.getOutputStream());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
                    
                    break;

                case 2:
                    
                    try {
            PrintStream saida;
            System.out.println("O cliente conectou ao servidor");

            servidor.cadastrarEntradas(nomeProd, codVal, indiceVazio);
            
            saida = new PrintStream(this.cliente.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
                    
                    break;

                case 3:
                    
                    try {
            PrintStream saida;
            System.out.println("O cliente conectou ao servidor");
            
            servidor.cadastrarVendas(nomeProd, codVal, indiceVazio);
            
            saida = new PrintStream(this.cliente.getOutputStream());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
                    
                    break;

                case 4:
                    
                    try {
            PrintStream saida;
            System.out.println("O cliente conectou ao servidor");
            
            servidor.relatorioVendas(nomeProd, codVal, indiceVazio);
            
            saida = new PrintStream(this.cliente.getOutputStream());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
                    
                    break;

                case 5:
                    
                    try {
            PrintStream saida;
            System.out.println("O cliente conectou ao servidor");
            
            servidor.mostraTodosProdutos(nomeProd, codVal, indiceVazio);
            
            saida = new PrintStream(this.cliente.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
                    
                    break;

                case 6:
                    JOptionPane.showMessageDialog(null, "Finalizando o sistema...");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Código inválido");
                    break;
            }

        } while (op != 6);
        
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 12345);
        
        Cliente c = new Cliente(socket);
        Thread t = new Thread((Runnable) c);
        t.start();
        
    }

}