public interface Observer {
    // void receiveLaneEvent(LaneEvent le);

    /**
     * recievePinsetterEvent()
     * 
     * defines the method for an object torecieve a pinsetter event
     */
    void receivePinsetterEvent(PinsetterEvent pe);
}
