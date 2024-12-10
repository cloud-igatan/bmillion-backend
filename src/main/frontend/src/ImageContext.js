
import React, { createContext, useState } from "react";

export const ImageContext = createContext();
export const ImageProvider = ({ children }) => {
    const [imageSrc, setImageSrc] = useState(null);

    return (
        <ImageContext.Provider value={{ imageSrc, setImageSrc }}>
            {children}
        </ImageContext.Provider>
    );
};
