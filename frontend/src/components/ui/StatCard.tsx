import { LucideIcon } from "lucide-react";

type StatCardColor = "blue" | "green" | "purple" | "orange" | "indigo";
type TrendDirection = "up" | "down";

interface StatCardProps {
  title: string;
  value: string | number;
  icon?: LucideIcon;
  trend?: TrendDirection;
  trendValue?: string;
  color?: StatCardColor;
}

const StatCard = ({
  title,
  value,
  icon: Icon,
  trend,
  trendValue,
  color = "blue",
}: StatCardProps) => {
  const colors: Record<
    StatCardColor,
    { bg: string; icon: string; gradient: string }
  > = {
    blue: {
      bg: "bg-blue-50",
      icon: "text-blue-600",
      gradient: "from-blue-500 to-blue-600",
    },
    green: {
      bg: "bg-green-50",
      icon: "text-green-600",
      gradient: "from-green-500 to-green-600",
    },
    purple: {
      bg: "bg-purple-50",
      icon: "text-purple-600",
      gradient: "from-purple-500 to-purple-600",
    },
    orange: {
      bg: "bg-orange-50",
      icon: "text-orange-600",
      gradient: "from-orange-500 to-orange-600",
    },
    indigo: {
      bg: "bg-indigo-50",
      icon: "text-indigo-600",
      gradient: "from-indigo-500 to-indigo-600",
    },
  };

  const colorClass = colors[color] || colors.blue;

  return (
    <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow duration-200">
      <div className="flex items-start justify-between">
        <div>
          <p className="text-sm font-medium text-gray-500">{title}</p>
          <p className="mt-2 text-3xl font-bold text-gray-900">{value}</p>
          {trend && (
            <div className="mt-2 flex items-center gap-1">
              <span
                className={`text-xs font-medium ${
                  trend === "up" ? "text-green-600" : "text-red-600"
                }`}
              >
                {trend === "up" ? "↑" : "↓"} {trendValue}
              </span>
              <span className="text-xs text-gray-500">vs last period</span>
            </div>
          )}
        </div>
        <div
          className={`w-12 h-12 rounded-xl bg-gradient-to-br ${colorClass.gradient} flex items-center justify-center shadow-lg`}
        >
          {Icon && <Icon className="w-6 h-6 text-white" />}
        </div>
      </div>
    </div>
  );
};

export default StatCard;
