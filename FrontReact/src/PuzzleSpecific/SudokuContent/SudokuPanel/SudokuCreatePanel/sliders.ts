interface inputSliderProps {
    label: string,
    propertyName: string,
    minValue: number,
    maxValue: number,
    initialValue: number,
    step: number,
    icon: string
}

export const sliders : inputSliderProps[] = [
    {
        label: "Difficulty",
        propertyName: "difficulty",
        minValue: 1.0,
        maxValue: 5.0,
        initialValue: 3.0,
        step: 0.1,
        icon: "LeaderboardIcon"
    }
]