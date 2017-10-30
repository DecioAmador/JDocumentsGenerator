package com.github.decioamador.jdocsgen.model.product;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Product implements Comparable<Product> {

    private static final Comparator<Price> COMP_CURRENY;

    static {
        COMP_CURRENY = (final Price p1, final Price p2) -> p1.getCurrency().getCurrencyCode()
                .compareTo(p2.getCurrency().getCurrencyCode());
    }

    private String uuid;
    private BasicInfo basicInfo;
    private Set<Price> prices;

    public Product(final String uuid, final String name, final String description) {
        this.uuid = Objects.requireNonNull(uuid);
        this.basicInfo = new BasicInfo(Objects.requireNonNull(name), Objects.requireNonNull(description));
    }

    public boolean addPrice(final Price price) {
        boolean result;
        if (prices == null) {
            prices = new TreeSet<>(COMP_CURRENY);
        }
        result = prices.add(price);
        return result;
    }

    public boolean removePrice(final Price price) {
        boolean result = false;
        if (prices != null) {
            result = prices.remove(price);
        }
        return result;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public void setBasicInfo(final BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setPrices(final Set<Price> prices) {
        this.prices = prices;
    }

    public Set<Price> getPrices() {
        return prices;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (uuid == null ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if (!uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(final Product o) {
        int result = 1;
        if (o != null) {
            if (this.basicInfo.getName().equals(o.basicInfo.getName())) {
                result = this.uuid.compareTo(o.uuid);
            } else {
                result = this.basicInfo.getName().compareTo(o.basicInfo.getName());
            }
        }
        return result;
    }

}
