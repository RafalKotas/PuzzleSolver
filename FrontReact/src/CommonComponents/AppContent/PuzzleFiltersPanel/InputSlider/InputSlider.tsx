//mui
import { Box, Grid, Input, Slider, Typography } from "@mui/material"
import WidthFullIcon from '@mui/icons-material/WidthFull'
import HeightIcon from '@mui/icons-material/Height';

//react
import React, { useEffect } from "react"
import { Leaderboard } from "@mui/icons-material";

interface OwnInputSliderProps {
    label: string,
    propertyName: string,
    minValue: number,
    initialValue: number,
    maxValue: number,
    step: number,
    icon: string,
    passValueToParent: (property: string, value: number | Array<number>) => void
}

const InputSlider : React.FC<OwnInputSliderProps> = ({label, propertyName,
     minValue, initialValue, maxValue, step, icon,
     passValueToParent}) => {

    function getIconByName(iconName: string): React.ReactNode {
      switch (iconName) {
        case "LeaderboardIcon":
          return <Leaderboard />
        case "HeightIcon":
          return <HeightIcon />
        case "WidthFullIcon":
          return <WidthFullIcon />
        default:
          return null;
      }
    }

    const [value, setValue] = React.useState<number | Array<number>>(initialValue)

    const handleSliderChange = (event: Event, newValue: number | number[]) => {
        setValue(newValue as number[])
    }

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setValue(Number(event.target.value))
    }

    const handleBlur = () => {
      const valueToCheck = Array.isArray(value) ? value[0] : value;

      if (valueToCheck < minValue) {
        setValue(minValue)
      } else if (valueToCheck > maxValue) {
        setValue(maxValue)
      }
    };

    useEffect(() => {
      passValueToParent(propertyName, value)
      //eslint-disable-next-line
    }, [value])

    return (
        <Box sx={{ width: 350 }}>
          <Typography id="input-slider" gutterBottom>
            {label}
          </Typography>
          <Grid container spacing={2} alignItems="center">
            <Grid item>
              {getIconByName(icon)}
            </Grid>
            <Grid item xs>
              <Slider
                max={maxValue}
                min={minValue}
                value={value}
                step={step}
                onChange={handleSliderChange}
                aria-labelledby="input-slider"
              />
            </Grid>
            <Grid item>
              <Input
                value={value}
                size="small"
                onChange={handleInputChange}
                onBlur={handleBlur}
                inputProps={{
                  step: step === 1 ? 5 : 0.1,
                  min: minValue,
                  max: maxValue,
                  type: "number",
                  "aria-labelledby": "input-slider",
                }}
              />
            </Grid>
          </Grid>
        </Box>
      );
}

export default InputSlider