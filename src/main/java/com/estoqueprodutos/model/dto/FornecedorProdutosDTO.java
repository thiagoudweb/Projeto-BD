package com.estoqueprodutos.model.dto;

import java.util.List;

public class FornecedorProdutosDTO {
    private Integer fornecedorId;
    private String fornecedorNome;
    private List<Integer> produtoIds;

    public FornecedorProdutosDTO() {}

    public FornecedorProdutosDTO(Integer fornecedorId, String fornecedorNome, List<Integer> produtoIds) {
        this.fornecedorId = fornecedorId;
        this.fornecedorNome = fornecedorNome;
        this.produtoIds = produtoIds;
    }

    public Integer getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Integer fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public String getFornecedorNome() {
        return fornecedorNome;
    }

    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
    }

    public List<Integer> getProdutoIds() {
        return produtoIds;
    }

    public void setProdutoIds(List<Integer> produtoIds) {
        this.produtoIds = produtoIds;
    }
}
