package com.ryan.github.webview.sample;

/**
 * Created by Ryan
 * on 2019/10/24
 */
public class Performance {

    private long navigationStart;
    private long unloadEventStart;
    private long unloadEventEnd;
    private long redirectStart;
    private long redirectEnd;
    private long fetchStart;
    private long domainLookupStart;
    private long domainLookupEnd;
    private long connectStart;
    private long connectEnd;
    private long secureConnectionStart;
    private long requestStart;
    private long responseStart;
    private long responseEnd;
    private long domLoading;
    private long domInteractive;
    private long domContentLoadedEventStart;
    private long domContentLoadedEventEnd;
    private long domComplete;
    private long loadEventStart;
    private long loadEventEnd;

    public long getNavigationStart() {
        return navigationStart;
    }

    public void setNavigationStart(long navigationStart) {
        this.navigationStart = navigationStart;
    }

    public long getUnloadEventStart() {
        return unloadEventStart;
    }

    public void setUnloadEventStart(long unloadEventStart) {
        this.unloadEventStart = unloadEventStart;
    }

    public long getUnloadEventEnd() {
        return unloadEventEnd;
    }

    public void setUnloadEventEnd(long unloadEventEnd) {
        this.unloadEventEnd = unloadEventEnd;
    }

    public long getRedirectStart() {
        return redirectStart;
    }

    public void setRedirectStart(long redirectStart) {
        this.redirectStart = redirectStart;
    }

    public long getRedirectEnd() {
        return redirectEnd;
    }

    public void setRedirectEnd(long redirectEnd) {
        this.redirectEnd = redirectEnd;
    }

    public long getFetchStart() {
        return fetchStart;
    }

    public void setFetchStart(long fetchStart) {
        this.fetchStart = fetchStart;
    }

    public long getDomainLookupStart() {
        return domainLookupStart;
    }

    public void setDomainLookupStart(long domainLookupStart) {
        this.domainLookupStart = domainLookupStart;
    }

    public long getDomainLookupEnd() {
        return domainLookupEnd;
    }

    public void setDomainLookupEnd(long domainLookupEnd) {
        this.domainLookupEnd = domainLookupEnd;
    }

    public long getConnectStart() {
        return connectStart;
    }

    public void setConnectStart(long connectStart) {
        this.connectStart = connectStart;
    }

    public long getConnectEnd() {
        return connectEnd;
    }

    public void setConnectEnd(long connectEnd) {
        this.connectEnd = connectEnd;
    }

    public long getSecureConnectionStart() {
        return secureConnectionStart;
    }

    public void setSecureConnectionStart(long secureConnectionStart) {
        this.secureConnectionStart = secureConnectionStart;
    }

    public long getRequestStart() {
        return requestStart;
    }

    public void setRequestStart(long requestStart) {
        this.requestStart = requestStart;
    }

    public long getResponseStart() {
        return responseStart;
    }

    public void setResponseStart(long responseStart) {
        this.responseStart = responseStart;
    }

    public long getResponseEnd() {
        return responseEnd;
    }

    public void setResponseEnd(long responseEnd) {
        this.responseEnd = responseEnd;
    }

    public long getDomLoading() {
        return domLoading;
    }

    public void setDomLoading(long domLoading) {
        this.domLoading = domLoading;
    }

    public long getDomInteractive() {
        return domInteractive;
    }

    public void setDomInteractive(long domInteractive) {
        this.domInteractive = domInteractive;
    }

    public long getDomContentLoadedEventStart() {
        return domContentLoadedEventStart;
    }

    public void setDomContentLoadedEventStart(long domContentLoadedEventStart) {
        this.domContentLoadedEventStart = domContentLoadedEventStart;
    }

    public long getDomContentLoadedEventEnd() {
        return domContentLoadedEventEnd;
    }

    public void setDomContentLoadedEventEnd(long domContentLoadedEventEnd) {
        this.domContentLoadedEventEnd = domContentLoadedEventEnd;
    }

    public long getDomComplete() {
        return domComplete;
    }

    public void setDomComplete(long domComplete) {
        this.domComplete = domComplete;
    }

    public long getLoadEventStart() {
        return loadEventStart;
    }

    public void setLoadEventStart(long loadEventStart) {
        this.loadEventStart = loadEventStart;
    }

    public long getLoadEventEnd() {
        return loadEventEnd;
    }

    public void setLoadEventEnd(long loadEventEnd) {
        this.loadEventEnd = loadEventEnd;
    }
}
