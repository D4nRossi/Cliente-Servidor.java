import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Servidor implements Runnable{
    
    public Socket cliente;

    public Servidor(Socket cliente){
        this.cliente = cliente;
    }
    
    //Cadastro de produtos
    public int cadastraProduto(String nomeProd[], double codVal[][], int indiceVazio) {
        char resp = 's', confirmaCad;
        int indiceCad, indiceNomeCad;
        double prCusto, valVenda, codCad;
        String nomeCad;

        do {
            if (indiceVazio < nomeProd.length) {
                nomeCad = JOptionPane.showInputDialog("Digite o nome do produto: ");
                //Verifica se o nome do produto já foi cadastrado
                indiceCad = procNome(nomeProd, codVal, indiceVazio, nomeCad);
                if (indiceCad != -1) {
                    //Mostra mensagem caso esteja cadastrado e informa onde
                    JOptionPane.showMessageDialog(null, "Produto já cadastrado com o código: " + codVal[1][indiceCad]);
                } else {
                    codCad = Double.parseDouble(JOptionPane.showInputDialog("Digite o código do produto"));
                    //Verifica se o código do produto já foi cadastrado
                    indiceNomeCad = procCod(nomeProd, codVal, indiceVazio, codCad);
                    if (indiceNomeCad != -1) {
                        //Caso sim, mostra o nome do produto que está usando o código
                        JOptionPane.showMessageDialog(null, "Código já cadastrado para o produto: " + nomeProd[indiceNomeCad]);
                    } else {
                        prCusto = Double.parseDouble(JOptionPane.showInputDialog("Digite o preço de custo: "));
                        valVenda = Double.parseDouble(JOptionPane.showInputDialog("Digite o valor de venda: "));

                        confirmaCad = (JOptionPane.showInputDialog(
                                "           Confirmação de cadastro" + "\n\n"
                                + "Código: " + codCad + "\n"
                                + "Nome do produto: " + nomeCad + "\n"
                                + "Preço de custo: R$" + prCusto + "\n"
                                + "Valor de venda: R$" + valVenda + "\n\n"
                                + "Confirma entrada dos dados (S/N)?")).charAt(0);

                        if ((confirmaCad == 's') || (confirmaCad == 'S')) {

                            execCad(nomeProd, codVal, indiceVazio, codCad, nomeCad, prCusto, valVenda);
                            indiceVazio = (indiceVazio + 1);
                        }
                    }
                }

                resp = (JOptionPane.showInputDialog("Deseja cadastrar outro produto (S/N)?")).charAt(0);
            } else {
                JOptionPane.showMessageDialog(null, "Limite de cadastros alcançado");
                resp = 'n';
            }
        } while ((resp == 's') || (resp == 'S'));
        return indiceVazio;
    }

//Verifica se o código do produto já está cadastrado - Se estiver, retorna a posição
    private int procCod(String nomeProd[], double codVal[][], int indiceVazio, double buscaCod) {
        int i = 0;

        while ((i < indiceVazio) && (buscaCod != codVal[1][i])) {
            i++;
        }
        if (buscaCod != codVal[1][i]) {
            return -1;
        } else {
            return i;
        }
    }

//Verifica se o nome do produto já está cadastrado - Se estiver, retorna a posição	
    private int procNome(String nomeProd[], double codVal[][], int indiceVazio, String buscaNome) {
        int i = 0;

        while ((i < indiceVazio) && (!(buscaNome.equalsIgnoreCase(nomeProd[i])))) {
            i++;
        }
        if (!(buscaNome.equalsIgnoreCase(nomeProd[i]))) {
            return -1;
        } else {
            return i;
        }
    }

//Cadastro de produtos (Execução de cadastro)
    private void execCad(String nomeProd[], double codVal[][], int indice, double cod, String nome,
            double prCusto, double valVenda) {
        double lucrUn;

        lucrUn = valVenda - prCusto;
        nomeProd[indice] = nome;
        codVal[1][indice] = cod;
        codVal[5][indice] = prCusto;
        codVal[6][indice] = valVenda;
        codVal[7][indice] = lucrUn;
    }

//Cadastro de entrada em estoque de produtos já cadastrados
    public void cadastrarEntradas(String nomeProd[], double codVal[][], int indiceVazio) {
        double codEntr;
        char resp = 's', confirmaEntr;
        int achouProd;
        double qtdadeEntr;

        do {
            codEntr = Double.parseDouble(JOptionPane.showInputDialog("Informe o código do produto: "));
            achouProd = procCod(nomeProd, codVal, indiceVazio, codEntr);
            if (achouProd != -1) {
                qtdadeEntr = Double.parseDouble(JOptionPane.showInputDialog("Informe a quantidade da entrada do produto: " + nomeProd[achouProd] + "\n"));

                confirmaEntr = (JOptionPane.showInputDialog(
                        "           Confirmação de entrada" + "\n\n"
                        + "Código: " + codVal[1][achouProd] + "\n"
                        + "Nome do produto: " + nomeProd[achouProd] + "\n"
                        + "Quantidade da entrada: " + qtdadeEntr + "\n"
                        + "Valor da entrada: R$" + (qtdadeEntr * codVal[5][achouProd]) + "\n\n"
                        + "Confirma entrada dos dados (S/N)?")).charAt(0);
                if ((confirmaEntr == 's') || (confirmaEntr == 'S')) {
                    execCadEntr(nomeProd, codVal, achouProd, codEntr, qtdadeEntr);
                }
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "                        O código informado: " + codEntr + " não foi localizado. \n"
                        + "          Certifique-se de que você digitou o código corretamente \n "
                        + "                             e que o produto esteja cadastrado.");
            }
            resp = (JOptionPane.showInputDialog("Deseja cadastrar outra entrada (S/N)?")).charAt(0);

        } while ((resp == 's') || (resp == 'S'));
    }

//Cadastro de entradas
    private void execCadEntr(String nomeProd[], double codVal[][], int indProd, double codEntr, double qtdade) {

        codVal[2][indProd] = (codVal[2][indProd] + qtdade);
        codVal[4][indProd] = (codVal[4][indProd] + qtdade);
    }

//Cadastro de vendas de produtos
    public void cadastrarVendas(String nomeProd[], double codVal[][], int indiceVazio) {
        double codVend;
        char resp = 's', confirmaVenda = 'n';
        int achouProd;
        double qtdadeVend, qtEstoque;

        do {
            codVend = Double.parseDouble(JOptionPane.showInputDialog("Informe o código do produto: "));
            
            achouProd = procCod(nomeProd, codVal, indiceVazio, codVend);
            if (achouProd != -1) {
                
                qtEstoque = verEstoque(codVal, achouProd);
                
                if (qtEstoque <= 0.0) {
                    JOptionPane.showMessageDialog(null, "O produto informado: " + nomeProd[achouProd] + " não está em estoque!");
                }
                else {
                    qtdadeVend = Double.parseDouble(JOptionPane.showInputDialog(""
                            + "Informe a quantidade vendida do produto: " + nomeProd[achouProd]));

                    if (qtdadeVend > qtEstoque) {
                        JOptionPane.showMessageDialog(null, "A quantidade em estoque do produto: " + nomeProd[achouProd] + " é somente: " + qtEstoque);
                    }
                    
                    if (qtdadeVend <= qtEstoque) {
                        
                        confirmaVenda = (JOptionPane.showInputDialog(
                                "Código: " + codVal[1][achouProd] + "\n"
                                + "Produto: " + nomeProd[achouProd] + "\n"
                                + "Quantidade Vendida: " + qtdadeVend + "\n"
                                + "Valor unitário: R$" + codVal[6][achouProd] + "\n"
                                + "Valor total: R$" + (codVal[6][achouProd] * qtdadeVend) + "\n"
                                + "Confirma dados da venda (S/N)?")).charAt(0);
                    }
                    if ((confirmaVenda == 's') || (confirmaVenda == 'S')) {
                        execCadVenda(codVal, achouProd, qtdadeVend);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "O código informado: " + codVend + " é inválido");
            }
            resp = (JOptionPane.showInputDialog("Deseja cadastrar outra venda (S/N)?")).charAt(0);

        } while ((resp == 's') || (resp == 'S'));

    }

//Verifica Quantidade do produto no estoque
    private double verEstoque(double codVal[][], int achouProd) {
        double estoque;

        estoque = codVal[4][achouProd];

        return estoque;
    }

//Cadastro de Vendas realizadas
    private void execCadVenda(double codVal[][], int indProd, double qtdade) {

        codVal[3][indProd] = (codVal[3][indProd] + qtdade);
        codVal[4][indProd] = (codVal[4][indProd] - qtdade);
    }

//Verifica se houveram movimentações (Compra ou venda) do produto	
    private int verMov(String nomeProd[], double codVal[][], int indiceVazio, int tipoMov, int indPesq) { //tipoMov é 2 compras, 3 vendas, 4 estoque 

        if (codVal[tipoMov][indPesq] > 0) {
            return indPesq;
        } else {
            return -1;
        }
    }

//Cálculo de valores de movimentação
    private double calcValores(String nomeProd[], double codVal[][], int indiceVazio, int tipoMov, int preco, int indPesq) {
        double valor = 0;
        int indice;

        indice = verMov(nomeProd, codVal, indiceVazio, tipoMov, indPesq);
        if (indice != -1) {
            valor = (valor + (codVal[preco][indice] * codVal[tipoMov][indice]));
        }
        return valor;
    }

//Relatório de vendas dos produtos
    public void relatorioVendas(String nomeProd[], double codVal[][], int indiceVazio) {
        int tipoMov = 3, indice = 0, indPesq = 0, i = 0;
        double valorVendas = 0, custoVendas = 0, faturamento = 0;

        do {

            indice = verMov(nomeProd, codVal, indiceVazio, tipoMov, indPesq);
            if (indice != -1) {

                JOptionPane.showMessageDialog(null,
                        "    Relatório de vendas/faturamento" + "\n\n"
                        + "Código: " + codVal[1][indice] + "\n"
                        + "Nome do produto: " + nomeProd[indice] + "\n"
                        + "Valor de venda: R$" + codVal[6][indice] + "\n"
                        + "Quantidade vendida: " + codVal[3][indice] + "\n"
                        + "Valor das vendas do produto: R$" + (codVal[3][indice] * codVal[6][indice]));
            }
            
            valorVendas = (calcValores(nomeProd, codVal, indiceVazio, tipoMov, 6, indPesq) + valorVendas);
            
            custoVendas = (calcValores(nomeProd, codVal, indiceVazio, tipoMov, 5, indPesq) + custoVendas);
            indPesq = (indPesq + 1);

            i++;
        } while (i < indiceVazio);

        faturamento = (valorVendas - custoVendas);

        JOptionPane.showMessageDialog(null,
                "  Relatório de vendas/faturamento" + "\n\n"
                + "Valor total das vendas: R$" + valorVendas + "\n"
                + "Lucro líquido: R$" + faturamento);
    }

//Mostra todos as informações cadastradas no sistema
    public void mostraTodosProdutos(String nomeProd[], double codVal[][], int indiceVazio) {
        int i = 0;

        do {

            JOptionPane.showMessageDialog(null,
                    "Código: " + codVal[1][i] + "\n"
                    + "Nome do produto: " + nomeProd[i] + "\n"
                    + "Preço de custo: R$" + codVal[5][i] + "\n"
                    + "Valor de venda: R$" + codVal[6][i] + "\n"
                    + "Quantidade em estoque: " + codVal[4][i] + "\n"
                    + "Quantidade comprada: " + codVal[2][i] + "\n"
                    + "Quantidade vendida: " + codVal[3][i] + "\n"
                    + "Valor das compras do produto: R$" + (codVal[2][i] * codVal[5][i]) + "\n"
                    + "Valor das vendas do produto: R$" + (codVal[3][i] * codVal[6][i]) + "\n"
                    + "Lucro obtido com vendas deste produto: R$" + ((codVal[3][i] * codVal[6][i]) - (codVal[5][i] * codVal[3][i])) + "\n"
                    + "Valor de custo em estoque do produto: R$" + (codVal[4][i] * codVal[5][i]) + "\n"
                    + "Valor de venda em estoque do produto: R$" + (codVal[4][i] * codVal[6][i]));

            i++;
        } while (i < indiceVazio);
    }

    public static void main(String[] args) throws IOException{
        ServerSocket servidor = new ServerSocket (12345);
        System.out.println("Porta 12345 aberta!");

        System.out.println("Aguardando conexão do cliente...");   

        while (true) {
          Socket cliente = servidor.accept();
          Servidor tratamento = new Servidor(cliente);
          Thread t = new Thread(tratamento);
          t.start();
          
        }
        
    }

    public void run(){
        System.out.println("Nova conexao com o cliente " + this.cliente.getInetAddress().getHostAddress());

        try {
            Scanner s = null;
            s = new Scanner(this.cliente.getInputStream());
            
            while(s.hasNextLine()){
                System.out.println(s.nextLine());
            }
            
            s.close();
            this.cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}