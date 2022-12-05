package sped.lcdpr;

import sped.lib.Block;
import sped.lib.Factory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class BlockQ extends Block {
    public static String BLOCK_NAME = "Q";
    private final List<RegisterQ100> registersQ100 = new ArrayList<>();
    private final List<RegisterQ200> registersQ200 = new ArrayList<>();

    public BlockQ(Factory factory) {
        super(BLOCK_NAME, factory);
    }

    public RegisterQ100 addRegisterQ100() {
        RegisterQ100 registerQ100 = (RegisterQ100) this.addNamedRegister(RegisterQ100.class);
        registersQ100.add(registerQ100);
        return registerQ100;
    }

    private RegisterQ200 addRegisterQ200() {
        RegisterQ200 registerQ200 = (RegisterQ200) this.addNamedRegister(RegisterQ200.class);
        registersQ200.add(registerQ200);
        return registerQ200;
    }

    public void calculateBalances() {
        AtomicReference<Double> saldo = new AtomicReference<>(0.0);
        registersQ100.forEach(registerQ100 -> {
            saldo.updateAndGet(value -> value + registerQ100.getValorEntrada() - registerQ100.getValorSaida());
            registerQ100.setSaldoFinal(saldo.get());
        });
    }

    private void removeQ200() {
        registersQ200.forEach(registerQ200 -> this.getRegisters().remove(registerQ200.getRegister()));
        registersQ200.clear();
    }

    public void generateQ200() {
        removeQ200();

        final AtomicReference<Double> saldoFinalMesAnterior = new AtomicReference<>(0.0);

        var map = registersQ100.stream()
                .collect(Collectors.groupingBy(
                        registerQ100 -> new SimpleDateFormat("yyyyMM").format(registerQ100.getDate())
                ));

        new TreeMap<>(map)
                .forEach((anoMes, registerQ100List) -> {
                    final RegisterQ200 registerQ200 = addRegisterQ200();

                    var mesAno = Integer.parseInt(anoMes.substring(4, 6) + anoMes.substring(0, 4)); //MMyyyy
                    registerQ200.setMesAno(mesAno);
                    registerQ200.setSaldoFinal(saldoFinalMesAnterior.get());

                    registerQ100List.forEach(registerQ100 -> {
                        registerQ200.incrementValorEntrada(registerQ100.getValorEntrada());
                        registerQ200.incrementValorSaida(registerQ100.getValorSaida());
                        registerQ200.incrementSaldoFinal(registerQ100.getValorEntrada() - registerQ100.getValorSaida());
                    });

                    saldoFinalMesAnterior.set(registerQ200.getSaldoFinal());
                });
    }
}
