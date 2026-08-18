package service;

public class GeradorCodigoProduto {

    private static int proximoCodigo = 1;

    public static String gerarCodigo(){
        String codigo = "CdP" + String.format("%05d", proximoCodigo);
        proximoCodigo++;
        return codigo;
    }

    public static void ajustarProximoCodigo(String ultimoCodigoExistente){
        try{
            int numero = Integer.parseInt(ultimoCodigoExistente.replace("CdP", ""));
            if (numero >= proximoCodigo) {
                proximoCodigo = numero + 1;
            }
        //Se o código estiver em outro formato, o sistema ira ignorar e manterá o contador atual
        } catch (NumberFormatException e ){

        }
    }
}
