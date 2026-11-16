package co.satingaconecta.pqrs.dao;

class PqrsDaoMemoriaTest extends PqrsDaoContrato {
    private final PqrsDao dao = new PqrsDaoMemoria();
    @Override PqrsDao dao() { return dao; }
}
