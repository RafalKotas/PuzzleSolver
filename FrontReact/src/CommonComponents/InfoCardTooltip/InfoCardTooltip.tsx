//mui
import { styled, Tooltip, tooltipClasses, TooltipProps} from "@mui/material"

const InfoCardTooltip = styled(({ className, ...props }: TooltipProps) => (
    <Tooltip {...props} classes={{ popper: className }} placement={props.placement}/>
  ))(({ theme }) => ({
    [`& .${tooltipClasses.tooltip}`]: {
      maxWidth: 180,
      fontSize: theme.typography.pxToRem(12),
      border: "1px solid #dadde9",
      padding: "5px",
      borderRadius: "10%"
    }
}));

export default InfoCardTooltip