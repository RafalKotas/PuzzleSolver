import { FormControlLabel, Switch } from "@mui/material"

interface ModeChangerProps {
    changeMode: (event : React.ChangeEvent<HTMLInputElement>) => void,
    modeSelected : string
}

const ModeChanger  : React.FC<ModeChangerProps> = ({changeMode, modeSelected}) => {
    
    const labelPlacementSide = () => {
        return modeSelected === "READ" ? "start" : "end"
    }

    const labelFromMode = () => {
        return modeSelected === "READ" ? "READ" : "CREATE"
    }
    
    return (
        <div>
            <FormControlLabel
                value={modeSelected}
                control={<Switch color="primary" onChange={(event: React.ChangeEvent<HTMLInputElement>) => changeMode(event)}/>}
                label={labelFromMode()}
                labelPlacement={labelPlacementSide()}
            />
        </div>
    )
}

export default ModeChanger