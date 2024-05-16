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
import java.time.LocalDate;
import java.awt.event.ActionEvent;

public class CadastroCliente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCliente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroCliente frame = new CadastroCliente();
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
	public CadastroCliente() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(250, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Digite o nome do Cliente");
		lblNewLabel.setBounds(53, 71, 142, 14);
		contentPane.add(lblNewLabel);
		
		txtCliente = new JTextField();
		txtCliente.setBounds(20, 96, 192, 20);
		contentPane.add(txtCliente);
		txtCliente.setColumns(10);
		
		
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 String nomeCliente = txtCliente.getText();
				 LocalDate dataCadastro = LocalDate.now();
			     
			     Connection conexao = null;
			     PreparedStatement stmt = null;
			     
			     
			     try {
			            
			            conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_vendas", "root", "");
			            
			            // Criando a instrução SQL de inserção
			            String sql = "INSERT INTO Cliente (nome, dtCadastro) VALUES (?, ?)";
			            stmt = conexao.prepareStatement(sql);
			            stmt.setString(1, nomeCliente);
			            stmt.setObject(2, dataCadastro);
			            
			            // Executando a instrução SQL de inserção
			            stmt.executeUpdate();
			            
			            // Feedback para o usuário
			            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
			            
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
		btnSalvar.setBounds(75, 127, 89, 23);
		contentPane.add(btnSalvar);
	}

}
