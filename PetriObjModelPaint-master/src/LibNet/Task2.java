package LibNet;
import PetriObj.*;
import java.util.ArrayList;

public class Task2 {
    public static int NUMBER_RUNS = 10;
    public static int MODELLING_TIME = 100000;

    public static void main(String[] args) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        double result = 0;
        for (int i = 0; i < NUMBER_RUNS; ++i) {
            PetriObjModel model = getTaskModel();
            model.setIsProtokol(false);
            model.go(MODELLING_TIME);
            result += model.getListObj().get(5).getNet().getListP()[3].getObservedMax() / (double)NUMBER_RUNS;
        }
        System.out.printf("Result: %f \n", result);
    }

    public static PetriObjModel getTaskModel() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriSim> system = new ArrayList<PetriSim>();

        system.add(new PetriSim(NetLibrary.CreateNetGenerator(
                40.0
        )));

        system.add(new PetriSim(NetLibrary.CreateNetFirstObject(
                6.0,
                24.0
        )));

        system.add(new PetriSim(NetLibrary.CreateNetSecondObject(
                3,
                "norm",
                60.0
        )));

        system.add(new PetriSim(NetLibrary.CreateNetFirstObject(
                7.0,
                24.0
        )));

        system.add(new PetriSim(NetLibrary.CreateNetSecondObject(
                3,
                "exp",
                100
        )));

        system.add(new PetriSim(NetLibrary.CreateNetFirstObject(
                5.0,
                24.0
        )));

        system.get(0).getNet().getListP()[1] = system.get(1).getNet().getListP()[0];
        system.get(1).getNet().getListP()[3] = system.get(2).getNet().getListP()[0];
        system.get(2).getNet().getListP()[1] = system.get(3).getNet().getListP()[0];
        system.get(3).getNet().getListP()[3] = system.get(4).getNet().getListP()[0];
        system.get(4).getNet().getListP()[1] = system.get(5).getNet().getListP()[0];

        return new PetriObjModel(system);
    }
    
    
}
