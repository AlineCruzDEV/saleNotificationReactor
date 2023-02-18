package org.example;

import org.example.notifications.CartNotification;
import org.example.notifications.SaleNotification;
import org.example.service.SaleService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SaleNotification saleNotification = new SaleNotification();
        SaleService saleService = new SaleService();
        CartNotification cartNotification = new CartNotification();

        saleNotification.getVendaFlux().delayElements(Duration.ofSeconds(5)).subscribe(sale -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(sale.getClientName() + " realizou uma compra");
        });

        cartNotification.getCartFlux().subscribe(product -> {
            System.out.println(Thread.currentThread().getName());
            if (product < 1 || product > 4)
                System.out.println("Produto inválido");
            else
                System.out.println("Novo produto inserido no carrinho: " + verifyProduct(product));
        });

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Digite seu nome");
            String clientName = scanner.next();
            int saveProduct = 0;
            List<Integer> productsId = new ArrayList<>();
            while (saveProduct != 4) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("Digite o produto que deseja adicionar ou 4 para finalizar");
                System.out.println(" 1 - bala \n 2 - refrigerante \n 3 - chocolate \n 4 - finalizar");
                Integer escolha = scanner.nextInt();
                if (escolha != 4) {
                    productsId.add(escolha);
                    cartNotification.send(escolha);
                }
                saveProduct = escolha;
            }
            saleService.saveSale(clientName, productsId, saleNotification);
            System.out.println("Compra realizada");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String verifyProduct(Integer product) {
        if (product == 1)
            return "bala";
        else if (product == 2)
            return "refrigerante";
        else if (product == 3)
            return "chocolate";
        else
            return "";
    }
}