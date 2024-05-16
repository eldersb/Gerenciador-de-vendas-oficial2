import java.sql.Statement;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;


	import java.awt.EventQueue;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import javax.swing.table.DefaultTableModel;

import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JTextField;
	import javax.swing.JComboBox;
	import javax.swing.DefaultComboBoxModel;
	import javax.swing.JButton;
	import javax.swing.JTable;
	import java.awt.Font;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;

	public class VendasProdutos extends JFrame {

		private static final long serialVersionUID = 1L;
		private JPanel contentPane;
		private JTable table;
		private JComboBox<String> comboCliente;
		private JComboBox<String> comboProduto;
		private JTextField txtQuant;
		private JTextField txtID;


		/**
		 * Launch the application.
		 */
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						VendasProdutos frame = new VendasProdutos();
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
		public VendasProdutos() {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 450, 300);
			setSize(700, 400);

			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			JLabel lblNewLabel_1 = new JLabel("Cliente");
			lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 18));
			lblNewLabel_1.setBounds(185, 11, 144, 14);
			contentPane.add(lblNewLabel_1);
			
			comboCliente = new JComboBox<>();
			comboCliente.setBounds(172, 36, 144, 22);
			contentPane.add(comboCliente);
			
			JLabel lblNewLabel_2 = new JLabel("Produto");
			lblNewLabel_2.setFont(new Font("Times New Roman", Font.PLAIN, 18));
			lblNewLabel_2.setBounds(377, 11, 134, 14);
			contentPane.add(lblNewLabel_2);
			
			comboProduto = new JComboBox<>();
			comboProduto.setBounds(354, 36, 134, 22);
			contentPane.add(comboProduto);
			
			JLabel lblNewLabel_3 = new JLabel("Quant");
			lblNewLabel_3.setBounds(567, 11, 46, 14);
			contentPane.add(lblNewLabel_3);
			
			 
			
			JButton btnAdicionar = new JButton("Adicionar");
			btnAdicionar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					    
				
				adicionarItem();
														
				    }
				         		
			});
			btnAdicionar.setBounds(20, 319, 89, 23);
			contentPane.add(btnAdicionar);
			
			table = new JTable();
			table.setBounds(10, 103, 538, 205);
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(10, 103, 638, 205);
			contentPane.add(scrollPane);
			
			
			// contentPane.add(table);
			
			txtQuant = new JTextField();
			txtQuant.setBounds(561, 36, 46, 20);
			contentPane.add(txtQuant);
			txtQuant.setColumns(10);
			
			JLabel lblNewLabel_4 = new JLabel("Pedidos do dia");
			lblNewLabel_4.setFont(new Font("Times New Roman", Font.PLAIN, 22));
			lblNewLabel_4.setBounds(283, 69, 172, 23);
			contentPane.add(lblNewLabel_4);
			
			JButton btnListarPedidos = new JButton("Listar Pedidos");
			btnListarPedidos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					listarPedidos();
				}
			});
			btnListarPedidos.setBounds(284, 319, 125, 23);
			contentPane.add(btnListarPedidos);
			
			JButton btnExcluir = new JButton("Excluir Registros");
			btnExcluir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					excluirItem();
					
				}
			});
			btnExcluir.setBounds(504, 319, 144, 23);
			contentPane.add(btnExcluir);
			
			JLabel lblNewLabel = new JLabel("ID");
			lblNewLabel.setBounds(20, 13, 46, 14);
			contentPane.add(lblNewLabel);
			
			txtID = new JTextField();
			txtID.setBounds(18, 37, 40, 20);
			contentPane.add(txtID);
			txtID.setColumns(10);
			
			exibirProximoIDPedido();

			
			  try {
		            populaComboCliente();
		            populaComboProduto();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		
		private void exibirProximoIDPedido() {
			try {
	            Connection conn = Conexao.conectar();

	            String sql = "SELECT MAX(Id) + 1 AS proximoId FROM Pedido";
	            PreparedStatement stmt = conn.prepareStatement(sql);
	            ResultSet rs = stmt.executeQuery();

	         
	            if (rs.next()) {
	                int proximoId = rs.getInt("proximoId");

	                // Define o próximo ID no campo txtID
	                txtID.setText(String.valueOf(proximoId));
	            }

	           
	            conn.close();
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Erro ao obter próximo ID do pedido: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
		
					
		
		 private void adicionarItem() {
		        try {
		            String clienteSelecionado = (String) comboCliente.getSelectedItem();
		            String produtoSelecionado = (String) comboProduto.getSelectedItem();
		            int quantidade = Integer.parseInt(txtQuant.getText());

		            if (clienteSelecionado == null || produtoSelecionado == null || quantidade <= 0) {
		                JOptionPane.showMessageDialog(null, "Preencha todos os campos corretamente.");
		                return;
		            }

		            Connection conn = Conexao.conectar();
		            conn.setAutoCommit(false);

		            int clienteId = obterClienteId(clienteSelecionado, conn);

		            double preco = 0;
		            int produtoId = 0;
		            PreparedStatement stmtProduto = conn.prepareStatement("SELECT Id, preco FROM Produto WHERE descricao = ?");
		            stmtProduto.setString(1, produtoSelecionado);
		            ResultSet rsProduto = stmtProduto.executeQuery();
		            if (rsProduto.next()) {
		                produtoId = rsProduto.getInt("Id");
		                preco = rsProduto.getDouble("preco");
		            } else {
		                JOptionPane.showMessageDialog(null, "Produto não encontrado.");
		                conn.rollback();
		                conn.close();
		                return;
		            }

		            double total = preco * quantidade;

		            PreparedStatement stmtPedido = conn.prepareStatement("INSERT INTO Pedido (dtCadastro, ClienteId) VALUES (NOW(), ?)", Statement.RETURN_GENERATED_KEYS);
		            stmtPedido.setInt(1, clienteId);
		            stmtPedido.executeUpdate();

		            ResultSet rsPedido = stmtPedido.getGeneratedKeys();
		            int pedidoId = 0;
		            if (rsPedido.next()) {
		                pedidoId = rsPedido.getInt(1);
		            } else {
		                JOptionPane.showMessageDialog(null, "Erro ao obter ID do pedido.");
		                conn.rollback();
		                conn.close();
		                return;
		            }

		            PreparedStatement stmtItem = conn.prepareStatement("INSERT INTO Item (PedidoId, ProdutoId, Preco, Quantidade) VALUES (?, ?, ?, ?)");
		            stmtItem.setInt(1, pedidoId);
		            stmtItem.setInt(2, produtoId);
		            stmtItem.setDouble(3, preco);
		            stmtItem.setInt(4, quantidade);
		            stmtItem.executeUpdate();

		            conn.commit();
		            conn.close();

		            JOptionPane.showMessageDialog(null, "Item adicionado ao pedido com sucesso.");
		            
		            exibirProximoIDPedido();


		        } catch (NumberFormatException e) {
		            JOptionPane.showMessageDialog(null, "Quantidade inválida.");
		        } catch (SQLException e) {
		            JOptionPane.showMessageDialog(null, "Erro ao adicionar item ao pedido: " + e.getMessage());
		            e.printStackTrace();
		        }
		    }
		 
		 
		  private int obterClienteId(String nomeCliente, Connection conn) throws SQLException {
		        int clienteId = 0;
		        PreparedStatement stmtCliente = conn.prepareStatement("SELECT Id FROM Cliente WHERE nome = ?");
		        stmtCliente.setString(1, nomeCliente);
		        ResultSet rsCliente = stmtCliente.executeQuery();
		        if (rsCliente.next()) {
		            clienteId = rsCliente.getInt("Id");
		        }
		        return clienteId;
		    }
		  
		  
		  private void excluirItem() {
		        int selectedRow = table.getSelectedRow();
		        if (selectedRow == -1) {
		            JOptionPane.showMessageDialog(null, "Selecione um pedido para excluir.");
		            return;
		        }

		        int pedidoId = (int) table.getValueAt(selectedRow, 0);

		        try {
		            Connection conn = Conexao.conectar();
		            conn.setAutoCommit(false);

		            PreparedStatement stmtItem = conn.prepareStatement("DELETE FROM Item WHERE PedidoId = ?");
		            stmtItem.setInt(1, pedidoId);
		            stmtItem.executeUpdate();

		            PreparedStatement stmtPedido = conn.prepareStatement("DELETE FROM Pedido WHERE Id = ?");
		            stmtPedido.setInt(1, pedidoId);
		            stmtPedido.executeUpdate();
		            
		            conn.commit();
		            
		            listarPedidos();

		            JOptionPane.showMessageDialog(null, "Pedido excluído com sucesso.");

		        } catch (SQLException e) {
		            e.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Erro ao excluir pedido: " + e.getMessage());
		        }
		  
		  }
		  
		  private void listarPedidos() {
			  	DefaultTableModel model = (DefaultTableModel) table.getModel();
			  	
			    model.setRowCount(0);
			    
			    if (model.getColumnCount() == 0) {
			        model.addColumn("ID");
			        model.addColumn("Data");
			        model.addColumn("Cliente");
			        model.addColumn("Produto");
			        model.addColumn("Preço");
			        model.addColumn("Quant");
			        model.addColumn("Total");
			    }
			  	
			  
		        try {
		            LocalDate dataAtual = LocalDate.now();
		            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		            String dataFormatada = dataAtual.format(formatter);
		            
		            Connection conn = Conexao.conectar();
		            String sql = "SELECT p.Id AS pedidoId, p.dtCadastro, c.nome AS cliente, pr.descricao AS produto, i.preco, i.quantidade, (i.preco * i.quantidade) AS total " +
		                     	"FROM Pedido p " +
		                     	"JOIN Cliente c ON p.ClienteId = c.Id " +
		                     	"JOIN Item i ON p.Id = i.PedidoId " +
		                     	"JOIN Produto pr ON i.ProdutoId = pr.Id " +
		                     	"WHERE DATE(p.dtCadastro) = ?" +
		                     	"ORDER BY p.Id ASC";
		            
		            PreparedStatement stmt = conn.prepareStatement(sql);
		            stmt.setString(1, dataFormatada);
		            ResultSet rs = stmt.executeQuery();
		            
		            table.getColumnModel().getColumn(0).setPreferredWidth(20);
		            table.getColumnModel().getColumn(1).setPreferredWidth(20);
		            table.getColumnModel().getColumn(1).setPreferredWidth(20);
		            table.getColumnModel().getColumn(1).setPreferredWidth(50);
		            table.getColumnModel().getColumn(4).setPreferredWidth(30);
		            table.getColumnModel().getColumn(5).setPreferredWidth(20);
		            table.getColumnModel().getColumn(6).setPreferredWidth(30);


		          
		            
		            while (rs.next()) {
		            	int pedidoId = rs.getInt("pedidoId");
		                String dataPedido = rs.getString("dtCadastro");
		                String cliente = rs.getString("cliente");
		                String produto = rs.getString("produto");
		                double preco = rs.getDouble("preco");
		                int quantidade = rs.getInt("quantidade");
		                double total = rs.getDouble("total");
		                
		                // Adiciona os dados do pedido à tabela
		                
		                model.addRow(new Object[]{pedidoId, dataPedido, cliente, produto, preco, quantidade, total});

		            }
		            
		            // Limpa as linhas existentes na tabela
		            table.setRowHeight(30);
		            
		            conn.close();
		        } catch (SQLException e) {
		            JOptionPane.showMessageDialog(null, "Erro ao listar pedidos: " + e.getMessage());
		            e.printStackTrace();
		        }
		    }

		 
		

				
		private void populaComboCliente() throws SQLException {
	        Connection conexao = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;

	        try {
	            // Conecta ao banco de dados (substitua "url", "usuario" e "senha" pelos valores reais)
	            conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_vendas", "root", "");

	            String sql = "SELECT nome FROM Cliente";
	            stmt = conexao.prepareStatement(sql);
	            rs = stmt.executeQuery();

	            // Cria um modelo para o JComboBox e adiciona os nomes dos clientes
	            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	            while (rs.next()) {
	                model.addElement(rs.getString("nome"));
	            }

	            // Define o modelo para o JComboBox
	            comboCliente.setModel(model);
	        } finally {
	            // Fecha a conexão com o banco de dados
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conexao != null) conexao.close();
	        }
	    }		
		
		private void populaComboProduto() throws SQLException {
	        Connection conexao = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;

	        try {
	            // Conecta ao banco de dados (substitua "url", "usuario" e "senha" pelos valores reais)
	            conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_vendas", "root", "");

	            String sql = "SELECT descricao FROM Produto";
	            stmt = conexao.prepareStatement(sql);
	            rs = stmt.executeQuery();

	            // Cria um modelo para o JComboBox e adiciona os nomes dos clientes
	            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	            while (rs.next()) {
	                model.addElement(rs.getString("descricao"));
	            }

	            // Define o modelo para o JComboBox
	            comboProduto.setModel(model);
	        } finally {
	            // Fecha a conexão com o banco de dados
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conexao != null) conexao.close();
	        }
	    }	
	}
		

