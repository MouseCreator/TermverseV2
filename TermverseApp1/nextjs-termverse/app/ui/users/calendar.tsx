'use client'
import { useState, useEffect } from 'react';

interface DayStudied {
    date: string;
    contributions: number;
}

export function Calendar() {
    const [daysStudied, setDaysStudied] = useState<DayStudied[]>([]);
    const [hoveredDate, setHoveredDate] = useState<Date | null>(null);

    useEffect(() => {
        // Fetch user contributions using the mock function
        getUserContributions(456)
            .then((data: DayStudied[]) => setDaysStudied(data))
            .catch((error) => console.error('Error fetching user contributions:', error));
    }, []);

    const getLatestWeeks = (count: number): Date[][] => {
        const weeks: Date[][] = [];
        const today = new Date();
        today.setHours(0,0,0,0);
        const startDate = new Date(today);
        startDate.setDate(startDate.getDate() - (today.getDay() + 7 * (count - 1)));

        for (let i = 0; i < count; i++) {
            const week: Date[] = [];
            for (let j = 0; j < 7; j++) {
                const date = new Date(startDate);
                date.setHours(0,0,0,0)
                date.setDate(startDate.getDate() + j);
                week.push(date);
            }
            weeks.push(week);
            startDate.setDate(startDate.getDate() + 7);
        }

        return weeks;
    };

    const formatDate = (date: Date): string => {
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    };

    const getContributions = (date: Date): number => {
        const formattedDate = formatDate(date);
        const studiedDay = daysStudied.find((day) => day.date === formattedDate);
        return studiedDay ? studiedDay.contributions : 0;
    };

    const weeks = getLatestWeeks(5);
    const startDate = weeks[0][0];
    const endDate = weeks[weeks.length - 1][6];

    const getMonthName = (month: number): string => {
        const monthNames = [
            'January', 'February', 'March', 'April', 'May', 'June',
            'July', 'August', 'September', 'October', 'November', 'December'
        ];
        return monthNames[month];
    };

    const calendarTitle = startDate.getMonth() === endDate.getMonth()
        ? getMonthName(startDate.getMonth())
        : `${getMonthName(startDate.getMonth())}-${getMonthName(endDate.getMonth())}`;
    return (
        <div className="flex flex-col content-center w-96">
            <h2 className="text-2xl font-bold mb-4">{calendarTitle}</h2>
            <div className="flex flex-col">
                <div className="flex mb-2">
                    <span className="w-4"/>
                    <span className="w-12 items-center">M</span>
                    <span className="w-12 items-center">T</span>
                    <span className="w-12 items-center">W</span>
                    <span className="w-12 items-center">T</span>
                    <span className="w-12 items-center">F</span>
                    <span className="w-12 items-center">S</span>
                    <span className="w-12 items-center">S</span>
                </div>
                {weeks.map((week, index) => (
                    <div key={index} className="flex">
                        {week.map((date) => (
                            <div
                                key={date.toISOString()}
                                className="relative w-12 h-12 flex justify-center items-center cursor-pointer"
                                onMouseEnter={() => setHoveredDate(date)}
                                onMouseLeave={() => setHoveredDate(null)}
                            >
                            <div className="absolute w-12 h-12 rounded-full flex justify-center items-center">
                                <div
                                    className={`w-10 h-10 rounded-full flex justify-center items-center ${
                                        getContributions(date) > 0 ? 'bg-orange-500 text-white' :
                                            new Date(Date.now()) < date ? 'bg-gray-200 text-gray-300' : 'bg-gray-200 text-gray-500'
                                    }`}
                                >{date.getDate()}
                                </div>
                            </div>
                                {hoveredDate?.getDate() === date.getDate() && hoveredDate?.getMonth() === date.getMonth() && (
                                    <div className="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-1 p-1 bg-black bg-opacity-80 text-white text-xs rounded whitespace-nowrap z-10">
                                        Contributions: {getContributions(date)}
                                    </div>

                                )}
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    );
}

export async function getUserContributions(id: number): Promise<Array<DayStudied>> {
    const users: { [id: number]: Array<DayStudied>; } = {};
    users[456] = [
        {
            date: "2024-03-13",
            contributions: 2
        },
        {
            date: "2024-04-01",
            contributions: 2
        },
        {
            date: "2024-04-02",
            contributions: 1
        }
    ]
    return users[id];
}

