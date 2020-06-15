package com.greathammer.eqm.util;

 /*
 * Created by fy on 19/04/2017.
 */
public class DeployPoint {
 
    private Long id;

    private Boolean isProductionVersion;

    private String cityName;

    private Double longitude;

    private Double latitude;

    private Integer audioWavIntensity;

    private Integer audioWavCount;

    private Boolean isPlayCountDown;

    private Boolean isShowLight;

    private Boolean isPlayAudio;

    private String companyName;

    private Long testFirstTime;

    private Long testTime;

    private Long switchScreensaverTimer;

    private Integer helloScreenShowTime;

    private Integer screensaverShowAfter;

    private Boolean isLoopForTest;

    private String com;

    public DeployPoint() {
    }

    public DeployPoint(Boolean isProductionVersion, String cityName, Double longitude, Double latitude, Integer audioWavIntensity, Integer audioWavCount, Boolean isPlayCountDown, Boolean isShowLight, Boolean isPlayAudio, String companyName, Long testFirstTime, Long testTime, Long switchScreensaverTimer, Integer helloScreenShowTime, Integer screensaverShowAfter, Boolean isLoopForTest, String com) {
        this.isProductionVersion = isProductionVersion;
        this.cityName = cityName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.audioWavIntensity = audioWavIntensity;
        this.audioWavCount = audioWavCount;
        this.isPlayCountDown = isPlayCountDown;
        this.isShowLight = isShowLight;
        this.isPlayAudio = isPlayAudio;
        this.companyName = companyName;
        this.testFirstTime = testFirstTime;
        this.testTime = testTime;
        this.switchScreensaverTimer = switchScreensaverTimer;
        this.helloScreenShowTime = helloScreenShowTime;
        this.screensaverShowAfter = screensaverShowAfter;
        this.isLoopForTest = isLoopForTest;
        this.com = com;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getProductionVersion() {
        return isProductionVersion;
    }

    public void setProductionVersion(Boolean productionVersion) {
        isProductionVersion = productionVersion;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getAudioWavIntensity() {
        return audioWavIntensity;
    }

    public void setAudioWavIntensity(Integer audioWavIntensity) {
        this.audioWavIntensity = audioWavIntensity;
    }

    public Integer getAudioWavCount() {
        return audioWavCount;
    }

    public void setAudioWavCount(Integer audioWavCount) {
        this.audioWavCount = audioWavCount;
    }

    public Boolean getPlayCountDown() {
        return isPlayCountDown;
    }

    public void setPlayCountDown(Boolean playCountDown) {
        isPlayCountDown = playCountDown;
    }

    public Boolean getShowLight() {
        return isShowLight;
    }

    public void setShowLight(Boolean showLight) {
        isShowLight = showLight;
    }

    public Boolean getPlayAudio() {
        return isPlayAudio;
    }

    public void setPlayAudio(Boolean playAudio) {
        isPlayAudio = playAudio;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getTestFirstTime() {
        return testFirstTime;
    }

    public void setTestFirstTime(Long testFirstTime) {
        this.testFirstTime = testFirstTime;
    }

    public Long getTestTime() {
        return testTime;
    }

    public void setTestTime(Long testTime) {
        this.testTime = testTime;
    }

    public Long getSwitchScreensaverTimer() {
        return switchScreensaverTimer;
    }

    public void setSwitchScreensaverTimer(Long switchScreensaverTimer) {
        this.switchScreensaverTimer = switchScreensaverTimer;
    }

    public Integer getHelloScreenShowTime() {
        return helloScreenShowTime;
    }

    public void setHelloScreenShowTime(Integer helloScreenShowTime) {
        this.helloScreenShowTime = helloScreenShowTime;
    }

    public Integer getScreensaverShowAfter() {
        return screensaverShowAfter;
    }

    public void setScreensaverShowAfter(Integer screensaverShowAfter) {
        this.screensaverShowAfter = screensaverShowAfter;
    }

    public Boolean getLoopForTest() {
        return isLoopForTest;
    }

    public void setLoopForTest(Boolean loopForTest) {
        isLoopForTest = loopForTest;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    @Override
    public String toString() {
        return "DeployPoint{" +
                "id=" + id +
                ", isProductionVersion=" + isProductionVersion +
                ", cityName='" + cityName + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", audioWavIntensity=" + audioWavIntensity +
                ", audioWavCount=" + audioWavCount +
                ", isPlayCountDown=" + isPlayCountDown +
                ", isShowLight=" + isShowLight +
                ", isPlayAudio=" + isPlayAudio +
                ", companyName='" + companyName + '\'' +
                ", testFirstTime=" + testFirstTime +
                ", testTime=" + testTime +
                ", switchScreensaverTimer=" + switchScreensaverTimer +
                ", helloScreenShowTime=" + helloScreenShowTime +
                ", screensaverShowAfter=" + screensaverShowAfter +
                ", isLoopForTest=" + isLoopForTest +
                ", com='" + com + '\'' +
                '}';
    }
}
