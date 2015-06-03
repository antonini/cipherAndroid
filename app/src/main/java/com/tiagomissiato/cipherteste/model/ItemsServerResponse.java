package com.tiagomissiato.cipherteste.model;

import java.util.List;

/**
 * Created by trigoleto on 6/3/15.
 * t.m.rigoleto@gmail.com
 */
public class ItemsServerResponse {

    List<Item> conteudo;

    public List<Item> getConteudo() {
        return conteudo;
    }

    public void setConteudo(List<Item> conteudo) {
        this.conteudo = conteudo;
    }
}
