import { ReactNode, HTMLAttributes } from "react";

interface CardProps extends HTMLAttributes<HTMLDivElement> {
  children: ReactNode;
  className?: string;
  padding?: boolean;
  hover?: boolean;
}

const Card = ({
  children,
  className = "",
  padding = true,
  hover = false,
  ...props
}: CardProps) => {
  return (
    <div
      className={`bg-white rounded-2xl shadow-sm border border-gray-100 ${
        padding ? "p-6" : ""
      } ${
        hover ? "transition-shadow duration-200 hover:shadow-md" : ""
      } ${className}`}
      {...props}
    >
      {children}
    </div>
  );
};

export default Card;
