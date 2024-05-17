# Gerenciador-de-vendas
 Gerenciador de vendas, clientes e produtos desenvolvido em Java Swing que permite adicionar clientes, produtos, criar pedidos e adicionar itens aos pedidos.

## Tecnologias Utilizadas
* Java
* Swing
* MySQL

## Como executar o projeto
* Instale o  Java Development Kit (JDK) na sua máquina.
* Configure um banco de dados MySQL local com as tabelas:
  
> CREATE database db_vendas;

> CREATE TABLE Cliente (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(60),
    dtCadastro DATE

);
> CREATE TABLE Produto (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL
);

> CREATE TABLE Pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dtCadastro DATE,
    ClienteId INT,
    FOREIGN KEY (ClienteId) REFERENCES Cliente(Id)
);

> CREATE TABLE Item (
    Id INT AUTO_INCREMENT PRIMARY KEY,
    PedidoId INT,
    ProdutoId INT,
    Quantidade int,
    Preco DECIMAL(10, 2),
    FOREIGN KEY (PedidoId) REFERENCES Pedido(Id),
    FOREIGN KEY (ProdutoId) REFERENCES Produto(Id)
);

* Abra o projeto em sua IDE Java preferida.
* Compile e execute a classe Principal.java para iniciar a aplicação.
