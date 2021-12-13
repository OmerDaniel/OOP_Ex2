import api.GeoLocation;

public class MyGeoLocation implements GeoLocation {
    double x,y,z;

    public void setMyGeoLocation(double X,double Y,double Z,double DISTANCE){
        x=X;
        y=Y;
        z=0.0;
    }


    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {
        return 0.0;
    }
    public MyGeoLocation copy(){
        MyGeoLocation c = new MyGeoLocation();
        c.x=this.x;
        c.y=this.y;
        c.z=this.z;
        return c;

    }
}
