package com.a1500fh.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 25/05/2017.
 */

public class Imagem {
    private List<Produto> produtosList = new ArrayList<>();
    private List<RegionOfInterest> roiList = new ArrayList<>();
    private Bitmap imagem;



    public List<Produto> getProdutosList() {
        return produtosList;
    }

    public void setProdutosList(List<Produto> produtosList) {
        this.produtosList = produtosList;
    }

    public List<RegionOfInterest> getRoiList() {
        return roiList;
    }

    public void setRoiList(List<RegionOfInterest> roiList) {
        this.roiList = roiList;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }
}
