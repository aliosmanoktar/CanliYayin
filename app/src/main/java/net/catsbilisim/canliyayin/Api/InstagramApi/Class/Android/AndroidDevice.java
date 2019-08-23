package net.catsbilisim.canliyayin.Api.InstagramApi.Class.Android;

import java.io.Serializable;
import java.util.UUID;

public class AndroidDevice implements Serializable {
    private UUID PhoneGuid;
    private UUID DeviceGuid;
    private UUID GoogleAdId = UUID.randomUUID();
    private UUID RankToken = UUID.randomUUID();
    private UUID AdId = UUID.randomUUID();
    private AndroidVersion AndroidVer = new AndroidVersion();
    private String AndroidBoardName;
    private String AndroidBootloader;
    private String DeviceBrand;
    private String DeviceId;
    private String DeviceModel;
    private String DeviceModelBoot;
    private String DeviceModelIdentifier;
    private String FirmwareBrand;
    private String FirmwareFingerprint;
    private String FirmwareTags;
    private String FirmwareType;
    private String HardwareManufacturer;
    private String HardwareModel;
    private String Resolution = "1080x1812";
    private String Dpi = "480dpi";

    public AndroidDevice setAndroidBoardName(String androidBoardName) {
        AndroidBoardName = androidBoardName;
        return this;
    }

    public AndroidDevice setAndroidBootloader(String androidBootloader) {
        AndroidBootloader = androidBootloader;
        return this;
    }

    public AndroidDevice setDeviceBrand(String deviceBrand) {
        DeviceBrand = deviceBrand;
        return this;
    }

    public AndroidDevice setDeviceId(String deviceId) {
        DeviceId = deviceId;
        return this;
    }

    public AndroidDevice setDeviceModel(String deviceModel) {
        DeviceModel = deviceModel;
        return this;
    }

    public AndroidDevice setDeviceModelBoot(String deviceModelBoot) {
        DeviceModelBoot = deviceModelBoot;
        return this;
    }

    public AndroidDevice setDeviceModelIdentifier(String deviceModelIdentifier) {
        DeviceModelIdentifier = deviceModelIdentifier;
        return this;
    }

    public AndroidDevice setFirmwareBrand(String firmwareBrand) {
        FirmwareBrand = firmwareBrand;
        return this;
    }

    public AndroidDevice setFirmwareFingerprint(String firmwareFingerprint) {
        FirmwareFingerprint = firmwareFingerprint;
        return this;
    }

    public AndroidDevice setFirmwareTags(String firmwareTags) {
        FirmwareTags = firmwareTags;
        return this;
    }

    public AndroidDevice setFirmwareType(String firmwareType) {
        FirmwareType = firmwareType;
        return this;
    }

    public AndroidDevice setHardwareManufacturer(String hardwareManufacturer) {
        HardwareManufacturer = hardwareManufacturer;
        return this;
    }

    public AndroidDevice setHardwareModel(String hardwareModel) {
        HardwareModel = hardwareModel;
        return this;
    }

    public AndroidDevice setResolution(String resolution) {
        Resolution = resolution;
        return this;
    }

    public AndroidDevice setDpi(String dpi) {
        Dpi = dpi;
        return this;
    }
    public AndroidDevice setPhoneGuid(UUID phoneGuid) {
        PhoneGuid = phoneGuid;
        return this;
    }

    public AndroidDevice setDeviceGuid(UUID deviceGuid) {
        DeviceGuid = deviceGuid;
        return this;
    }

    public UUID getPhoneGuid() {
        return PhoneGuid;
    }

    public UUID getDeviceGuid() {
        return DeviceGuid;
    }

    public UUID getGoogleAdId() {
        return GoogleAdId;
    }

    public UUID getRankToken() {
        return RankToken;
    }

    public UUID getAdId() {
        return AdId;
    }

    public AndroidVersion getAndroidVer() {
        return AndroidVer;
    }

    public String getAndroidBoardName() {
        return AndroidBoardName;
    }

    public String getAndroidBootloader() {
        return AndroidBootloader;
    }

    public String getDeviceBrand() {
        return DeviceBrand;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getDeviceModel() {
        return DeviceModel;
    }

    public String getDeviceModelBoot() {
        return DeviceModelBoot;
    }

    public String getDeviceModelIdentifier() {
        return DeviceModelIdentifier;
    }

    public String getFirmwareBrand() {
        return FirmwareBrand;
    }

    public String getFirmwareFingerprint() {
        return FirmwareFingerprint;
    }

    public String getFirmwareTags() {
        return FirmwareTags;
    }

    public String getFirmwareType() {
        return FirmwareType;
    }

    public String getHardwareManufacturer() {
        return HardwareManufacturer;
    }

    public String getHardwareModel() {
        return HardwareModel;
    }

    public String getResolution() {
        return Resolution;
    }

    public String getDpi() {
        return Dpi;
    }
}
