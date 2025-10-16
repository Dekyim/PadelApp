package models;

public enum MetodoPago {
    EFECTIVO("efectivo"),
    TARJETA("tarjeta"),
    TRANSFERENCIA("transferencia"),
    MERCADO_PAGO("mercado_pago");

    private final String value;

    MetodoPago(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MetodoPago fromString(String value) {
        for (MetodoPago m : MetodoPago.values()) {
            if (m.value.equals(value)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Método de pago inválido: " + value);
    }
}