export interface Promotion {
  id: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  status?: string;
  upToDiscount: number;
  upToBonus?: number;
  imageName: string;
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
