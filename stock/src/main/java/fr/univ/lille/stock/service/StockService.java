package fr.univ.lille.stock.service;
import fr.univ.lille.stock.kafka.OrderSubmittedEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockService {

    private final ConcurrentHashMap<String, Integer> stock = new ConcurrentHashMap<>();

    public StockService() {
        // стартовые значения
        stock.put("tttt", 1000);
        stock.put("cafe", 50);
        stock.put("the", 30);
    }

    public Map<String, Integer> all() {
        return stock;
    }

    public void decrement(String libelle, int qte) {
        stock.merge(libelle, -qte, Integer::sum);
    }
    public void applyOrder(OrderSubmittedEvent event) {
        if (event == null || event.lignes == null) return;

        for (OrderSubmittedEvent.LigneDto l : event.lignes) {
            if (l == null || l.libelle == null || l.quantite == null) continue;
            stock.compute(l.libelle, (k, v) -> {
                int current = (v == null) ? 0 : v;
                int next = current - l.quantite;
                return Math.max(next, 0);
            });
        }
    }
}