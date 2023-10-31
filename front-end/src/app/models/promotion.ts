export interface Promotion {
  id?: number;
  name: string;
  description: string;
  startDate: Date;
  endDate: Date;
  status?: string;
  upToDiscount: number;
  upToBonus: number;
  imageName?: string;
  imageType?: string;
  image: string;
  promotionDetails: PromotionDetails[];

}


 export interface PromotionDetails {
    id?: number;
    promotionId?: number;
    productName: string;
    productPrice: number;
    productDiscount?: number;
    productBonus?: number;
    imageName?: string;
    imageType?: string;
    image: string;

}




// private Integer id;
// private String name;
// private String description;
// private LocalDateTime startDate;
// private LocalDateTime endDate;
// private String status;
// private Double upToDiscount;
// private Double upToBonus;
// private String imageName;
// private String imageType;
// private String image;
