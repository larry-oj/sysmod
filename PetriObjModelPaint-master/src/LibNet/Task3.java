package LibNet;
import PetriObj.*;
import java.util.ArrayList;

public class Task3 {
    public static int NUMBER_RUNS = 10;
    public static int MODELLING_TIME = 1000;

    public static void main(String[] args) throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        double earnings = 0;
        double lost = 0;
        double meanQueueTime = 0;
        for (int i = 0; i < NUMBER_RUNS; ++i) {
            PetriObjModel model = getTaskModel();
            
            model.setIsProtokol(false);
            model.go(MODELLING_TIME);

            lost += model.getListObj().get(0).getNet().getListP()[3].getObservedMax();
            earnings += model.getListObj().get(0).getNet().getListP()[6].getObservedMax();

            PetriNet firstCity = model.getListObj().get(0).getNet();
            PetriNet secondCity = model.getListObj().get(1).getNet();
            
            double numWaitedInQueues = (
                    firstCity.getListP()[2].getMark() +
                    firstCity.getListT()[3].getBuffer() +
                    firstCity.getListT()[3].getBuffer() +
                    firstCity.getListP()[6].getObservedMax() / 20.0 +
                    secondCity.getListP()[2].getMark() +
                    secondCity.getListT()[3].getBuffer() +
                    secondCity.getListT()[3].getBuffer());
            
            double averageQueuesSize = firstCity.getListP()[2].getMean() + secondCity.getListP()[2].getMean();
            
            meanQueueTime += (averageQueuesSize * MODELLING_TIME /
                    Math.max(numWaitedInQueues, 1)) /
                    (double)NUMBER_RUNS;
        }

        earnings = earnings / (double)NUMBER_RUNS;
        lost = lost / (double)NUMBER_RUNS;

        System.out.printf("Earnings: %f\n", earnings);
        System.out.printf("Lost: %f\n", lost);
        System.out.printf("Average time in line: %f", meanQueueTime);
    }

    public static PetriObjModel getTaskModel() throws ExceptionInvalidNetStructure, ExceptionInvalidTimeDelay {
        ArrayList<PetriSim> system = new ArrayList<PetriSim>();

        system.add(new PetriSim(NetLibrary.CreateNetBus(
                20,
                0.5,
                20.0,
                30.0,
                5.0,
                30,
                25,
                1,
                0,
                "unif")));

        system.add(new PetriSim(NetLibrary.CreateNetBus(
                20,
                0.5,
                20.0,
                30.0,
                5.0,
                30,
                25,
                0,
                1,
                "unif")));

        system.get(0).getNet().getListP()[7] = system.get(1).getNet().getListP()[4];
        system.get(0).getNet().getListP()[8] = system.get(1).getNet().getListP()[5];
        system.get(1).getNet().getListP()[3] = system.get(0).getNet().getListP()[3];
        system.get(1).getNet().getListP()[7] = system.get(0).getNet().getListP()[4];
        system.get(1).getNet().getListP()[6] = system.get(0).getNet().getListP()[6];
        system.get(1).getNet().getListP()[8] = system.get(0).getNet().getListP()[5];
        return new PetriObjModel(system);
    }
}
