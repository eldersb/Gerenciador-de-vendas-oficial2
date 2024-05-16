import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class CadastroProduto extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtDescricao;
	private JTextField txtPreco;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroProduto frame = new CadastroProduto();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CadastroProduto() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(250, 250);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Digite a descrição do produto:");
		lblNewLabel.setBounds(38, 33, 186, 14);
		contentPane.add(lblNewLabel);
		
		txtDescricao = new JTextField();
		txtDescricao.setBounds(38, 57, 167, 20);
		contentPane.add(txtDescricao);
		txtDescricao.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Digite o preco do produto:");
		lblNewLabel_1.setBounds(40, 102, 167, 14);
		contentPane.add(lblNewLabel_1);
		
		txtPreco = new JTextField();
		txtPreco.setBounds(40, 127, 164, 20);
		contentPane.add(txtPreco);
		txtPreco.setColumns(10);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 String descricaoProduto = txtDescricao.getText();
			     String preco = txtPreco.getText();
			     
			     Connection conexao = null;
			     PreparedStatement stmt = null;
			     
			     try {
			            // Configurando a conexão
			            conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_vendas", "root", "");
			            
			            // Criando a instrução SQL de inserção
			            String sql = "INSERT INTO Produto (descricao, preco) VALUES (?, ?)";
			            stmt = conexao.prepareStatement(sql);
			            stmt.setString(1, descricaoProduto);
			            stmt.setString(2, preco);
			            
			            // Executando a instrução SQL de inserção
			            stmt.executeUpdate();
			            
			            // Feedback para o usuário
			            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
			            
			        } catch (SQLException ex) {
			            ex.printStackTrace();
			            // Tratamento de exceção
			        } finally {
			            try {
			                // Fechando recursos
			                if (stmt != null) {
			                    stmt.close();
			                }
			                if (conexao != null) {
			                    conexao.close();
			                }
			            } catch (SQLException ex) {
			                ex.printStackTrace();
			            }
			        }
			     
			}
		});
		btnSalvar.setBounds(73, 162, 89, 23);
		contentPane.add(btnSalvar);
	}
}
