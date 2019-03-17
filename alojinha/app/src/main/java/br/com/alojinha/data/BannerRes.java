package br.com.alojinha.data;

import java.util.List;

import br.com.alojinha.model.Banner;

public class BannerRes extends AbstractRes{

    private List<Banner> banner;

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }
}
