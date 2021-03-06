ALTER TABLE tb_refeicao_realizada CHANGE id_refeicao_realizada 
id_refeicao_realizada INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE tb_pretensao_refeicao CHANGE id_pretensao_refeicao 
id_pretensao_refeicao INT(10) UNSIGNED NOT NULL AUTO_INCREMENT;

alter table tb_pessoa_role add column 
id_pessoa_role int(10) unsigned primary key AUTO_INCREMENT;

insert into tb_dia(id_dia, nm_dia) values (1, "Domingo");
insert into tb_dia(id_dia, nm_dia) values (2, "Segunda-feira");
insert into tb_dia(id_dia, nm_dia) values (3, "Terça-feira");
insert into tb_dia(id_dia, nm_dia) values (4, "Quarta-feira");
insert into tb_dia(id_dia, nm_dia) values (5, "Quinta-feira");
insert into tb_dia(id_dia, nm_dia) values (6, "Sexta-feira");
insert into tb_dia(id_dia, nm_dia) values (7, "Sábado");

INSERT INTO tb_refeicao (id_refeicao, nm_tipo_refeicao, hr_fim, hr_inicio, hr_pretensao, nr_dia_previsto_pretensao) VALUES
(1, 'Almoço', '14:30:00', '10:30:00', '7:00:00', 1),
(2, 'Jantar', '19:30:00', '17:00:00', '7:00:00', 1);

INSERT INTO tb_curso(id_curso, nm_curso) VALUES('1', 'Superior em Telemática');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('2', 'Integrado em Mineração');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('3', 'Integrado em Informática');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('4', 'Integrado em Petróleo e Gás');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('5', 'Integrado em MSI');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('6', 'Superior em Matemática');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('7', 'Superior em Construção de Edifícios');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('8', 'Subsequente em Informática');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('9', 'Superior em física');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('10', 'Subsequente em MSI');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('11', 'Subsequente em Mineração');
INSERT INTO tb_curso(id_curso, nm_curso) VALUES('12', 'Segurança do Trabalho');

-- Funcionários
INSERT INTO tb_pessoa (tp_pessoa, id_pessoa, is_ativo, nm_email, nm_keyauth, nm_pessoa, nm_senha) VALUES
(1, 517, b'1', 'admin@nutrif.com', '244679736B782A9F06B5E1241AFB99E9C33B3FEBE8588105B0640C2FD9145F13', 'admin', 'aWZwYjIwMTU='),
(1, 518, b'1', 'caest@nutrif.com', '8B8B90C10085E67E0537A84A286587C828F08D79738CA3230444158D37F184FB', 'caest', 'Y2Flc3RydQ==');

INSERT INTO tb_funcionario (id_funcionario, id_pessoa) VALUES
(517, 517),
(518, 518);

INSERT INTO tb_role (id_role, nm_role) VALUES
(1, 'admin'),
(2, 'inspetor'),
(3, 'comensal');

INSERT INTO tb_pessoa_role (fk_id_role, fk_id_pessoa) VALUES (1, 517), (2, 518);

-- Atualização: 20160509
UPDATE nutrif.tb_aluno SET is_acesso = b'0';
