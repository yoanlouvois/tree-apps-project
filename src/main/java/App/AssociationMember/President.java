package App.AssociationMember;

public enum President {
    INSTANCE;

    private Member president;

    public void setPresident(Member president) {
        this.president = president;
    }

    public Member getPresident() {
        return president;
    }

    public boolean isPresidentSet() {
        return president != null;
    }
}
