import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.awt.event.ActionEvent;

public class ListagemProdutos extends JFrame {

	// private static final long serialVersionUID = 1L;
	// private static final String USUARIO = null;
	// private static final String URL_CONEXAO = null;
	// private static final String SENHA = null;
	 private JPanel contentPane;
	 private JTable table;
	 private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListagemProdutos frame = new ListagemProdutos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	 public ResultSet pesquisarProduto(Connection conexao, String column, String termoPesquisa) throws SQLException {
		    String sql = "SELECT * FROM Produto WHERE " + column + " = ?";
		    PreparedStatement stmt = conexao.prepareStatement(sql);
		    stmt.setString(1, termoPesquisa);
		    return stmt.executeQuery();
		}

	/**
	 * Create the frame.
	 */
	public ListagemProdutos() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNovo = new JButton("NOVO");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CadastroProduto novoProduto = new CadastroProduto();
				novoProduto.setVisible(true);
			}
		});
		btnNovo.setBounds(10, 27, 81, 23);
		contentPane.add(btnNovo);
		
		JButton btnPesquisar = new JButton("PESQUISAR");
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 Object[] options = {"ID", "descricao"};
	                int choice = JOptionPane.showOptionDialog(ListagemProdutos.this, "Escolha o tipo de pesquisa:", "Tipo de Pesquisa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

	                String termoPesquisa = null;
	                String column = null;
	                if (choice == JOptionPane.YES_OPTION) {
	                    termoPesquisa = JOptionPane.showInputDialog(ListagemProdutos.this, "Digite o ID do produto:");
	                    column = "Id";
	                } else if (choice == JOptionPane.NO_OPTION) {
	                    termoPesquisa = JOptionPane.showInputDialog(ListagemProdutos.this, "Digite o nome do produto:");
	                    column = "descricao";
	                }
			
	               
				 if (termoPesquisa != null && !termoPesquisa.isEmpty()) {
	                    try {
	                        
	                        ResultSet rs = pesquisarProduto(Conexao.conectar(), column, termoPesquisa);
	                        
	                        if (rs.next()) {
	                            int id = rs.getInt("Id");
	                            String descricao = rs.getString("descricao");
	                            BigDecimal preco = rs.getBigDecimal("preco");
	                            JOptionPane.showMessageDialog(ListagemProdutos.this, "ID: " + id + "\nDescricao: " + descricao + "\nPreço do produto: " + preco);
	                        }else {
	                            JOptionPane.showMessageDialog(ListagemProdutos.this, "Nenhum produto encontrado com o ID fornecido.");
	                        } 
	                        } catch (SQLException ex) {
	                            JOptionPane.showMessageDialog(ListagemProdutos.this, "Erro ao pesquisar o produto: " + ex.getMessage());
	                        } } else {
	                            JOptionPane.showMessageDialog(ListagemProdutos.this, "Termo de pesquisa não fornecido.");
	                        }
	                        	                        	                       	                    								
				
			}
		});
		btnPesquisar.setBounds(99, 27, 108, 23);
		contentPane.add(btnPesquisar);
		
		JButton btnExcluir = new JButton("EXCLUIR");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int selectedRow = table_1.getSelectedRow();
		        if (selectedRow == -1) {
		            JOptionPane.showMessageDialog(null, "Selecione um produto para excluir.");
		            return;
				
			}
		        
		        int id = (int) table_1.getValueAt(selectedRow, 0);
		        String nome = (String) table_1.getValueAt(selectedRow, 1);
		        
		        int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o produto " + nome + "?", "Confirmação de exclusão", JOptionPane.YES_NO_OPTION);
		        
		        if (confirm == JOptionPane.YES_OPTION) {
		            // Executar a exclusão do produto
		            if (excluirProduto(id)) {
		                JOptionPane.showMessageDialog(null, "Produto excluído com sucesso.");
		                // Recarregar a tabela após a exclusão
		                carregarProdutos();
		            } else {
		                JOptionPane.showMessageDialog(null, "Erro ao excluir produto.");
		            }
		        }
		        
			}

			private boolean excluirProduto(int id) {
				Connection conexao = Conexao.conectar();
			    if (conexao != null) {
			        PreparedStatement stmt = null;
			        try {
			            String sql = "DELETE FROM Produto WHERE id = ?";
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
		btnExcluir.setBounds(217, 27, 89, 23);
		contentPane.add(btnExcluir);
				
		table_1 = new JTable();
		table_1.setBounds(20, 69, 388, 164);
		contentPane.add(table_1);
		
		JButton btnAtualizar = new JButton("ATUALIZAR");
		btnAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarProdutos();
			}
		});
		btnAtualizar.setBounds(316, 27, 108, 23);
		contentPane.add(btnAtualizar);
		
		 carregarProdutos();
	}
	
	private void carregarProdutos() {
        
		DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Descricao");
        model.addColumn("Preco");

        Connection conexao = Conexao.conectar();
        if (conexao != null) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "SELECT * FROM Produto";
                stmt = conexao.prepareStatement(sql);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("Id");
                    String descricao = rs.getString("descricao");
                    BigDecimal preco = rs.getBigDecimal("preco");
                    model.addRow(new Object[]{id, descricao, preco});
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
        }else {
            System.out.println("Não foi possível estabelecer conexão com o banco de dados.");
        }

        table_1.setModel(model);
        
        System.out.println("Carregamento de produtos concluído.");
    }
}
