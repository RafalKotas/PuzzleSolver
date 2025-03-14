import { faCalculator, faCircleCheck, faFloppyDisk, faSquare, faTag, faWandSparkles, faXmark, IconDefinition } from "@fortawesome/free-solid-svg-icons"
import { nonogramBoardMarks } from "../../../../../../../store/puzzleLogic/nonogram"

export const nonogramSolverActionsNames : string[] = ["COLOUR", "PLACE_X", "MARK", "CORRECT RANGES"]

export type nonogramActionsNames = "COLOUR" | "PLACE_X" | "MARK" | "CORRECT RANGES" | "CUSTOM SOLVER" | "SAVE SOLUTION" | "COMPARE WITH SOLUTION"

export interface actionProp {
    name: nonogramActionsNames,
    icon: IconDefinition,
    mark: nonogramBoardMarks
}

export const actionsProps: actionProp[] = [
    {
        name: "COLOUR",
        icon: faSquare,
        mark: "O"
    },
    {
        name: "PLACE_X",
        icon: faXmark,
        mark: "X"
    },
    {
        name: "MARK",
        icon: faTag,
        mark: "-"
    },
    {
        name: "CUSTOM SOLVER",
        icon: faWandSparkles,
        mark: "-"
    },
    {
        name: "SAVE SOLUTION",
        icon: faFloppyDisk,
        mark: "-"
    },
    {
        name: "COMPARE WITH SOLUTION",
        icon: faCircleCheck,
        mark: "-"
    },
    {
        name: "CORRECT RANGES",
        icon: faCalculator,
        mark: "-"
    }
]