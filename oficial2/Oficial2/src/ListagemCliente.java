import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class ListagemCliente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 
					ListagemCliente frame = new ListagemCliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	 public ResultSet pesquisarCliente(Connection conexao, String column, String termoPesquisa) throws SQLException {
		    String sql = "SELECT * FROM Cliente WHERE " + column + " = ?";
		    PreparedStatement stmt = conexao.prepareStatement(sql);
		    stmt.setString(1, termoPesquisa);
		    return stmt.executeQuery();
		}

	/**
	 * Create the frame.
	 */
	public ListagemCliente() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNovo = new JButton("NOVO");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CadastroCliente novoCadastro = new CadastroCliente();
				novoCadastro.setVisible(true);
			}
		});
		btnNovo.setBounds(10, 41, 69, 23);
		contentPane.add(btnNovo);
		
		JButton btnPesquisar = new JButton("PESQUISAR");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				  Object[] options = {"ID", "Nome"};
	                int choice = JOptionPane.showOptionDialog(ListagemCliente.this, "Escolha o tipo de pesquisa:", "Tipo de Pesquisa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

	                String termoPesquisa = null;
	                String column = null;
	                if (choice == JOptionPane.YES_OPTION) {
	                    termoPesquisa = JOptionPane.showInputDialog(ListagemCliente.this, "Digite o ID do cliente:");
	                    column = "Id";
	                } else if (choice == JOptionPane.NO_OPTION) {
	                    termoPesquisa = JOptionPane.showInputDialog(ListagemCliente.this, "Digite o nome do cliente:");
	                    column = "nome";
	                }
			
	               
				 if (termoPesquisa != null && !termoPesquisa.isEmpty()) {
	                    try {
	                        
	                        ResultSet rs = pesquisarCliente(Conexao.conectar(), column, termoPesquisa);
	                        
	                        if (rs.next()) {
	                            int id = rs.getInt("Id");
	                            String nome = rs.getString("nome");
	                            String dtCadastro = rs.getString("dtCadastro");
	                            JOptionPane.showMessageDialog(ListagemCliente.this, "ID: " + id + "\nNome: " + nome + "\nData de Cadastro: " + dtCadastro);
	                        }else {
	                            JOptionPane.showMessageDialog(ListagemCliente.this, "Nenhum cliente encontrado com o ID fornecido.");
	                        } 
	                        } catch (SQLException ex) {
	                            JOptionPane.showMessageDialog(ListagemCliente.this, "Erro ao pesquisar o cliente: " + ex.getMessage());
	                        } } else {
	                            JOptionPane.showMessageDialog(ListagemCliente.this, "Termo de pesquisa não fornecido.");
	                        }
	                        	                        	                       	                    	       	   				
			}
		});
		btnPesquisar.setBounds(89, 41, 109, 23);
		contentPane.add(btnPesquisar);
		
		JButton btnExcluir = new JButton("EXCLUIR");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int selectedRow = table.getSelectedRow();
		        if (selectedRow == -1) {
		            JOptionPane.showMessageDialog(null, "Selecione um cliente para excluir.");
		            return;
				
			}
		        
		        int id = (int) table.getValueAt(selectedRow, 0);
		        String nome = (String) table.getValueAt(selectedRow, 1);
		        
		        int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o cliente " + nome + "?", "Confirmação de exclusão", JOptionPane.YES_NO_OPTION);
		        
		        if (confirm == JOptionPane.YES_OPTION) {
		            // Executar a exclusão do cliente
		            if (excluirCliente(id)) {
		                JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso.");
		                // Recarregar a tabela após a exclusão
		                carregarClientes();
		            } else {
		                JOptionPane.showMessageDialog(null, "Erro ao excluir cliente.");
		            }
		        }
		    }

			private boolean excluirCliente(int id) {
				Connection conexao = Conexao.conectar();
			    if (conexao != null) {
			        PreparedStatement stmt = null;
			        try {
			            String sql = "DELETE FROM Cliente WHERE id = ?";
			            stmt = conexao.prepareStatement(sql);
			            stmt.setInt(1, id);
			            int rowsAffected = stmt.executeUpdate();
			            return rowsAffected > 0;
			        } catch (SQLException e) {
			            e.printStackTrace();
			            return false;
			        } finally {
			            try {
			                if (stmt != null) stmt.close();
			                conexao.close();
			            } catch (SQLException e) {
			                e.printStackTrace();
			            }
			        }
			    }
			    return false;
			}

		        
		});
		btnExcluir.setBounds(208, 41, 91, 23);
		contentPane.add(btnExcluir);
		
		table = new JTable();
		table.setBounds(10, 99, 414, 151);
		contentPane.add(table);
		
		JButton btnNewButton = new JButton("ATUALIZAR");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarClientes();
			}
		});
		btnNewButton.setBounds(309, 41, 115, 23);
		contentPane.add(btnNewButton);
		
		carregarClientes();
	}
	
	private void carregarClientes() {
        
		DefaultTableModel model =  (DefaultTableModel) table.getModel();
		
	    model.setRowCount(0);
	    
	    if (model.getColumnCount() == 0) {
	    	model.addColumn("ID");
	        model.addColumn("Nome");
	        model.addColumn("Data de Cadastro");
	        

	    }
						

        Connection conexao = Conexao.conectar();
        if (conexao != null) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "SELECT * FROM Cliente";
                stmt = conexao.prepareStatement(sql);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("Id");
                    String nome = rs.getString("nome");
                    String dtCadastro = rs.getString("dtCadastro");
                    model.addRow(new Object[]{id, nome, dtCadastro});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    conexao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        table.setModel(model);
    }
}
