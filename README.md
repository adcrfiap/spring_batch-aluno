# Projeto Batch Aluno - FIAP

## Pré-requisito

* Java 11;
* Para o Windows, é importante ter instalado o [MongoDB Community Server](https://www.mongodb.com/try/download/community?tck=docs_server), caso os testes sejam feitos local.

## Configurar arquivo application.yml

spring.data.mongodb:
* uri -> uri de conexão do mongoDB (default mongodb://localhost:27017)
* databse -> base de dados (default alunos)

## Arquivo de entrada

O arquivo `./src/main/resources/lista-alunos.txt` contem os dados para a inserção no mongodb.

## Clonar o projeto

    git clone https://github.com/adcrfiap/spring_batch-aluno.git
  
