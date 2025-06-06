package br.pucrs.nomeusuario.exemplo.infrastructure;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import br.pucrs.nomeusuario.exemplo.domain.models.*;
import br.pucrs.nomeusuario.exemplo.domain.persistence.*;

@Repository
public class EstoqueRepoMemo implements IEstoqueRepositorio{
    private List<ItemDeEstoqueModel> itens;
    private IProdutoRepositorio produtos;

    @Autowired
    public EstoqueRepoMemo(IProdutoRepositorio produtos){
        this.produtos = produtos;
        this.itens = new LinkedList<>();

        ProdutoModel p = produtos.consultaPorId(10);
        itens.add(new ItemDeEstoqueModel(100, p, 11, 5, 50));
        p = produtos.consultaPorId(20);
        itens.add(new ItemDeEstoqueModel(200, p, 22, 5, 50));
        p = produtos.consultaPorId(40);
        itens.add(new ItemDeEstoqueModel(400, p, 44, 10, 100));
        p = produtos.consultaPorId(50);
        itens.add(new ItemDeEstoqueModel(500, p, 55, 5, 40));
    }

    @Override
    public List<ProdutoModel> todos() {
        return produtos.todos();
    }

    @Override
    public List<ProdutoModel> todosComEstoque() {
        return itens.stream()
            .filter(it->it.getQuantidade() > 0)
            .map(it->it.getProduto())
            .toList();
    }

    @Override
    public int quantidadeEmEstoque(long id) {
        return itens.stream()
            .filter(it->it.getProduto().getId() == id)
            .map(it->it.getQuantidade())
            .findAny()
            .orElse(-1);
    }

    @Override
    public void baixaEstoque(long id, int qtdade) {
        ItemDeEstoqueModel item = itens.stream()
            .filter(it->it.getProduto().getId() == id)
            .findAny()
            .orElse(null);
        if (item == null){
            throw new IllegalArgumentException("Produto inexistente");
        }
        if (item.getQuantidade() < qtdade){
            throw new IllegalArgumentException("Quantidade em estoque insuficiente");
        }
        int novaQuantidade = item.getQuantidade() - qtdade;
        item.setQuantidade(novaQuantidade);
    }
}

