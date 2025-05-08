package aero.minova.cas.service.model;

public interface DBFileMD5View {
    String getKeyText();
    byte[] getValueMD5();
    byte[] getDefaultValueMD5();
}