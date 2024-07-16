import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;

class ExchangeRateResponse {
    Map<String, Double> conversion_rates;
}

public class ConversorMoedas {

    public static void main(String[] args) {
        ConversorMoedas conversor = new ConversorMoedas();
        conversor.run();
    }

    public void run() {
        Scanner entrada = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("0.00");

        while (true) {
            int option = 0;
            String moedaOrigem = "";
            String moedaDestino = "";

            // Menu para escolha da moeda de origem
            while (option <= 0 || option > 6) {
                System.out.println("Escolha a moeda de origem:");
                System.out.println("1. ARS (Peso Argentino)");
                System.out.println("2. BOB (Boliviano)");
                System.out.println("3. BRL (Real Brasileiro)");
                System.out.println("4. CLP (Peso Chileno)");
                System.out.println("5. COP (Peso Colombiano)");
                System.out.println("6. USD (Dólar Americano)");
                System.out.print("Insira o número correspondente à moeda de origem: ");
                option = entrada.nextInt();

                switch (option) {
                    case 1:
                        moedaOrigem = "ARS";
                        break;
                    case 2:
                        moedaOrigem = "BOB";
                        break;
                    case 3:
                        moedaOrigem = "BRL";
                        break;
                    case 4:
                        moedaOrigem = "CLP";
                        break;
                    case 5:
                        moedaOrigem = "COP";
                        break;
                    case 6:
                        moedaOrigem = "USD";
                        break;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                        break;
                }
            }

            option = 0; // Reset option for the next choice

            // Menu para escolha da moeda de destino
            while (option <= 0 || option > 6) {
                System.out.println("Escolha a moeda de destino:");
                System.out.println("1. ARS (Peso Argentino)");
                System.out.println("2. BOB (Boliviano)");
                System.out.println("3. BRL (Real Brasileiro)");
                System.out.println("4. CLP (Peso Chileno)");
                System.out.println("5. COP (Peso Colombiano)");
                System.out.println("6. USD (Dólar Americano)");
                System.out.print("Insira o número correspondente à moeda de destino: ");
                option = entrada.nextInt();

                switch (option) {
                    case 1:
                        moedaDestino = "ARS";
                        break;
                    case 2:
                        moedaDestino = "BOB";
                        break;
                    case 3:
                        moedaDestino = "BRL";
                        break;
                    case 4:
                        moedaDestino = "CLP";
                        break;
                    case 5:
                        moedaDestino = "COP";
                        break;
                    case 6:
                        moedaDestino = "USD";
                        break;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                        break;
                }
            }

            try {
                // Obter a taxa de câmbio atual da API
                double taxaCambio = getExchangeRate(moedaOrigem, moedaDestino);

                // Solicita o valor ao usuário
                System.out.print("Digite o valor em " + moedaOrigem + ": ");
                double valorOrigem = entrada.nextDouble();

                // Converte o valor da moeda de origem para a moeda de destino
                double valorConvertido = valorOrigem * taxaCambio;

                // Exibe a taxa de câmbio e o valor convertido
                System.out.println("Taxa de câmbio de " + moedaOrigem + " para " + moedaDestino + ": " + df.format(taxaCambio));
                System.out.println("Valor convertido em " + moedaDestino + ": " + df.format(valorConvertido));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Pergunta ao usuário se deseja realizar outra conversão
            System.out.print("Deseja realizar outra conversão? (1 - Sim / 2 - Não): ");
            int resposta = entrada.nextInt();

            if (resposta == 2) {
                break;
            } else if (resposta != 1) {
                System.out.println("Opção inválida, tente novamente.");
            }
        }

        System.out.println("Programa encerrado.");
    }

    private double getExchangeRate(String fromCurrency, String toCurrency) throws Exception {
        // URL da API (exemplo usando exchangerate-api.com)
        String apiKey = "b2fa22b4440165c03d11e62e"; // Substitua pelo seu API Key
        String url = String.format("https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + fromCurrency);

        // Cria a requisição HTTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Envia a requisição e obtém a resposta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verifica o código de resposta
        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha ao obter a taxa de câmbio: " + response.statusCode());
        }

        // Parse da resposta JSON
        Gson gson = new Gson();
        ExchangeRateResponse rateResponse = gson.fromJson(response.body(), ExchangeRateResponse.class);

        // Verifica se o mapa de taxas de câmbio está nulo
        if (rateResponse.conversion_rates == null) {
            throw new RuntimeException("Resposta inválida da API, o campo 'conversion_rates' está nulo.");
        }

        // Retorna a taxa de câmbio correta
        Double exchangeRate = rateResponse.conversion_rates.get(toCurrency);
        if (exchangeRate == null) {
            throw new RuntimeException("Moeda de destino inválida: " + toCurrency);

        return exchangeRate;
    }
}