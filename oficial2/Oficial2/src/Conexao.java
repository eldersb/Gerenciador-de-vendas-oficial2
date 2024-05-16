import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	 private static Connection connection = null;

	    public static Connection conectar() {
	        String servidor = "jdbc:mysql://localhost:3306/db_vendas";
	        String usuario = "root";
	        String senha = "";
	        String driver = "com.mysql.cj.jdbc.Driver";

	        try {
	            Class.forName(driver);
	            connection = DriverManager.getConnection(servidor, usuario, senha);
	            System.out.println("Conexão bem-sucedida com o banco de dados.");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Driver MySQL não encontrado: " + e.getMessage());
	        } catch (SQLException e) {
	            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
	        }

	        return connection;
	    }
}
