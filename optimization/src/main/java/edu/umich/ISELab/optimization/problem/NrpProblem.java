package edu.umich.ISELab.optimization.problem;

import edu.umich.ISELab.core.backlog.Project;
import edu.umich.ISELab.core.grooming.NrpBase;
import edu.umich.ISELab.core.util.RandomUtils;
import edu.umich.ISELab.core.util.UUIDUtils;
import edu.umich.ISELab.evaluation.Objective;
import edu.umich.ISELab.optimization.solution.NrpSolution;
import edu.umich.ISELab.optimization.solution.Solution;
import edu.umich.ISELab.optimization.variables.NrpVariable;
import org.apache.commons.io.FilenameUtils;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.io.File;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class NrpProblem extends Problem {

    private static final long serialVersionUID = 8592620571577924372L;
    /**
     * Define the minimum number of grooming in a solution
     */
    protected int minSolutionSize = 5;
    /**
     * Define the maximum number of grooming in a solution
     */
    protected int maxSolutionSize = 10;
    protected EvolutionListener listener;
    protected Project project;
    protected List<NrpBase> selectedRefactorings;
    protected File file;


    public NrpProblem(Project project, List<Objective> objectives, List<NrpBase> selectedRefactorings) {
        super(objectives);

        //Verify the arguments
        checkNotNull(project, "The project cannot be null");
        this.selectedRefactorings = selectedRefactorings;
        this.project = project;
        // JMetal's Settings
        setNumberOfVariables(1);
    }

    public NrpProblem(File file,Project project, List<Objective> objectives, List<NrpBase> selectedRefactorings) {
        super(objectives);

        //Verify the arguments
        checkNotNull(project, "The project cannot be null");

        this.selectedRefactorings = selectedRefactorings;
        this.project = project;
        this.file = file;

        // JMetal's Settings
        setNumberOfVariables(1);
    }

    public NrpProblem(File file, List<Objective> objectives, List<NrpBase> selectedRefactorings) {
        super(objectives);

        checkNotNull(file, "The 'filename' cannot be null");

        this.selectedRefactorings = selectedRefactorings;
        this.file = file;

        /*try {
            this.project = FileReaderUtils.read(file);
            this.project.setDesignMetrics(DesignMetricsUtil.calculate(this.project));
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/

        // JMetal's Settings
        setNumberOfVariables(1);
    }

    public void evaluate(Solution solution) {

        UUIDUtils.restart();

        //Project copy = ProjectObjectUtils.copy(project);

        List<NrpBase> refactorings = ((NrpVariable) solution.getVariableValue(0)).getRefactorings();

        //List<NrpBase> valids = NrpUtils.getValids(copy, refactorings);

        /*while (valids.size() > maxSolutionSize) {
            valids.remove(valids.size() - 1);
        }

        while (valids.size() < minSolutionSize) {

            NrpBase nrpBase = RandomUtils.getRandomRefactoring(selectedRefactorings);

            try {
                NrpUtils.apply(copy, nrpBase);
                valids.add(nrpBase);
            } catch (Exception ex) {  }
        }*/

        /*((NrpVariable) solution.getVariableValue(0)).setRefatorings(valids);

        // ===============================
        // Calculate the Fitness Function
        // ===============================
        try {
            copy.setDesignMetrics(DesignMetricsUtil.calculate(copy));
            calculateFitnessFunction(solution, copy);
        } catch (Exception ex) {
            for (int i = 0; i < objectives.size(); i++) {
                solution.setObjective(i, Double.MAX_VALUE);
            }
        }

        if (listener != null) listener.evaluated();
    }*/
    }

    public void calculateFitnessFunction(Solution solution, Project refactored) {

        for (int i = 0; i < objectives.size(); i++) {
            solution.setObjective(i, objectives.get(i).calculate(project, refactored, ((NrpVariable) solution.getVariableValue(0)).getRefactorings()));
        }
    }

    /**
     * Create a random solution that represents a list of refactorings
     *
     * @return a random solution
     */
    public NrpSolution createSolution() {

        // Generating the amount of grooming that this solution will have
        int numberOfRefactorings = JMetalRandom.getInstance().nextInt(minSolutionSize, maxSolutionSize);

        // Create a grooming variable for saving the list of refactorings
        NrpVariable variable = new NrpVariable();

        for (int i = 0; i < numberOfRefactorings; i++) {
            variable.getRefactorings().add(RandomUtils.getRandomRefactoring(selectedRefactorings));
        }

        // Generate a solution
        NrpSolution solution = new NrpSolution(this);

        // Save the variable (list of refactorings) in the solution
        solution.setVariableValue(0, variable);

        return solution;
    }

    public int getMinSolutionSize() {
        return minSolutionSize;
    }

    public void setMinSolutionSize(int minSolutionSize) {
        this.minSolutionSize = minSolutionSize;
    }

    public int getMaxSolutionSize() {
        return maxSolutionSize;
    }

    public void setMaxSolutionSize(int maxSolutionSize) {
        this.maxSolutionSize = maxSolutionSize;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void addEvolutionListener(NrpProblem.EvolutionListener listener) {
        this.listener = listener;
    }

    public List<NrpBase> getSelectedRefactorings() {
        return selectedRefactorings;
    }

    public void setSelectedRefactorings(List<NrpBase> selectedRefactorings) {
        this.selectedRefactorings = selectedRefactorings;
    }

    @Override
    public String getName() {
        return "Refactoring Problem";
    }

    public String getFileName() {
        return FilenameUtils.getBaseName(file.getAbsolutePath());
    }

    public File getFile() {
        return file;
    }

    public interface EvolutionListener {
        void evaluated();
    }
}
