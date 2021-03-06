import os
import shutil
import glob
from PIL import Image

def parser():
    rootDir = str(input("Informe o diretório de origem:\n"))
    destDir = str(input("Informe o diretório de destino:\n"))

    for dirName, subdirList, fileList in os.walk(rootDir):
        print("Diretório encontrado: %s" % dirName)
        has_image = len(glob.glob(dirName + "\*.jpg"))
        if has_image != 0:
            dirMatr = dirName.find("-")
            if dirMatr != -1:
                matricula = dirName.split("-")
                matricula = matricula[-1]
                matricula = matricula.strip()
                dirCopy = destDir + os.sep + matricula
                shutil.copytree(dirName, dirCopy)
                for fname in fileList:
                    print("\t%s" % fname)
            else:
                nomeAluno = dirName.split(os.sep)
                nomeAluno = nomeAluno[-1].strip()

                arquivo = open(destDir + os.sep + "alunos_sem_matricula.txt", "a")
                arquivo.write(nomeAluno + "\n")
                arquivo.close()

                print(nomeAluno, " - Não possue matrícula")

def renomearFotoPerfil():
    diretorio = str(input("Informe o diretório:\n"))
    for dirName, subdirList, fileList in os.walk(diretorio):
        if dirName != diretorio: 
            if dirName.endswith("_") == False:
                nomeFoto = dirName + os.sep + fileList[0]
                nomeFotoNovo = dirName + os.sep + "perfil.jpg"
                os.rename(nomeFoto, nomeFotoNovo)
                print("Origem - Arquivo: ", nomeFoto, ", Destino -> ", nomeFotoNovo)

def parserSemTraco():
    rootDir = str(input("Informe o diretório de origem:\n"))
    destDir = str(input("Informe o diretório de destino:\n"))

    for dirName, subdirList, fileList in os.walk(rootDir):
        print("Diretorio encontrado: %s" % dirName)
        has_image = len(glob.glob(dirName + "\*.jpg"))
        if has_image != 0:
            dirMatr = dirName.find("-")
            if dirMatr == -1:
                matricula = dirName.split(" ")[-1]
                if matricula.isdigit():
                    matricula = matricula.strip()
                    dirCopy = destDir + os.sep + matricula
                    shutil.copytree(dirName, dirCopy)
                    for fname in fileList:
                        print("\t%s" % fname)

def redimensionarImagem():
    diretorio = str(input("Informe o diretório:\n"))    
    width = 1024
    height = 682
    print("Padrão: Largura: ", width, ", Altura: ", height)
    
    for dirName, subdirList, fileList in os.walk(diretorio):
        if dirName != diretorio:
            
            qtdArquivos = len(fileList)
            print("Quantidade de arquivos: ", qtdArquivos)
            
            for file in fileList:
                
                img = Image.open(dirName + os.sep + file)
                img = img.resize((width, height), Image.ANTIALIAS)
                img.save(dirName + os.sep + file)
                print(file," - redimensionado.")

def limparImagensReconhecimento():
    diretorio = str(input("Informe o diretório:\n"))    
    for dirName, subdirList, fileList in os.walk(diretorio):
        if dirName != diretorio:
            qtdArquivos = len(fileList)
            print("Quantidade de arquivos: ", qtdArquivos)
            
            for file in fileList:
                print("Analisando arquivo: ", file)
                if (file.find("perfil")<0):
                        os.remove(dirName + os.sep + file)
                        print("Removido")

def parserNovo():
    rootDir = str(input("Informe o diretório de origem:\n"))
    destDir = str(input("Informe o diretório de destino:\n"))

    for dirName, subdirList, fileList in os.walk(rootDir):
        for fileName in fileList:
            matricula = fileName.split(" ")
            matricula = matricula[-1].split(".")
            matricula = matricula[0]

            if matricula.isdigit():
                matricula = matricula.strip()
                dirFile = dirName + os.sep + fileName
                dirCopy = destDir + os.sep + matricula
                os.mkdir(dirCopy)
                shutil.copy(dirFile, dirCopy)
                print(fileName + " transferido com sucesso.")

def main():
    pergunta = eval(input("Para acessar o parser, digite 1. \n Para acessar o parserSemTraço, digite 2. \n Para renomear as fotos de perfil, digite 3. \n Para redimensionar as fotos, digite 4. \n Para remover fotos de reconhecimento, digite 5. \n Para usar o parser novo, digite 6.\n"))
    if pergunta == 1:
        parser()
    elif pergunta == 2:
        parserSemTraco()
    elif pergunta == 3:
        renomearFotoPerfil()
    elif pergunta == 4:
        redimensionarImagem()
    elif pergunta == 5:
        limparImagensReconhecimento()
    elif pergunta == 6:
        parserNovo()

if __name__ == "__main__":
    main()
